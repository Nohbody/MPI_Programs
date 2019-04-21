
import tensorflow as tf
import tensorlayer as tl
import gym
import numpy as np
import time,os,gc

D = None  # 80*80

render = False 
resume = True

batch_size = 10
model_file_name = "model_pong_cnn.npz"
np.set_printoptions(threshold=np.nan)

gamma = 0.99 # reward discount 

def prepro(I):

    """ prepro 210x160x3 uint8 frame into 6400 (80x80) 1D float vector """
    I = I[35:195] # crop
    I = I[::2,::2,0] # -> (80,80) , gray 
    
    # I == 144  -> True, False, ... 
    # I[I == 144] = 0 , set pixel to 0 which have original value 144 or 109
    I[I == 144] = 0 # erase background (background type 1)
    I[I == 109] = 0 # erase background (background type 2)
    
    I[I != 0] = 1 # everything else (paddles, ball) just set to 1
    # showNpArrayData(I)
    
    # final return : flatten 1D float array
    # return I.astype(np.float).ravel()
    return I 



def createNN():
    # observation for training and inference
    states_batch_pl = tf.placeholder(tf.float32, shape=[None, D[0],D[1] , 1  ], name = 'states_batch_pl' )
    # print "states_batch_pl dim: " , states_batch_pl.shape

    network = tl.layers.InputLayer(states_batch_pl, name='input_layer')
    # policy network
    network = tl.layers.Conv2dLayer(network,
            act = tf.nn.relu, shape = [5, 5, 1, 32],  # 32 filters
            strides=[1, 2, 2, 1], padding='SAME', name ='cnn_layer1')    

    network = tl.layers.PoolLayer(network,
            ksize=[1, 2, 2, 1], strides=[1, 2, 2, 1],
            padding='SAME', pool = tf.nn.max_pool, name ='pool_layer1',)

    network = tl.layers.Conv2dLayer(network,
            act = tf.nn.relu, shape = [5, 5, 32, 64], # 64 features for each 5x5 patch
            strides=[1, 2, 2, 1], padding='SAME', name ='cnn_layer2')  

    network = tl.layers.PoolLayer(network,
            ksize=[1, 2, 2, 1], strides=[1, 2, 2, 1],
            padding='SAME', pool = tf.nn.max_pool, name ='pool_layer2',) 

    network = tl.layers.FlattenLayer(network, name='flatten_layer')   # output: (?, 3136) 
    # network = tl.layers.DropoutLayer(network, keep=0.5, name='drop1') # output: (?, 3136)
    
    network = tl.layers.DenseLayer(network, n_units=256, act = tf.nn.relu, name='relu1')   # output: (?, 256)
    # network = tl.layers.DropoutLayer(network, keep=0.5, name='drop2') # output: (?, 256)

    # tf.identity means theta*x 
    network = tl.layers.DenseLayer(network, n_units=3, act = tf.identity, name='output_layer')    # output: (?, 10)
     

    probs = network.outputs
    # use to sample action
    sampling_prob = tf.nn.softmax(probs)
    
    actions_batch_pl = tf.placeholder(tf.int32, shape=[None])
    discount_rewards_batch_pl = tf.placeholder(tf.float32, shape=[None])

    # loss for Policy Gradient Network.
    # param1: The network outputs without softmax. This function implements softmax inside.
    loss = tl.rein.cross_entropy_reward_loss(probs, actions_batch_pl, discount_rewards_batch_pl)

    learning_rate = 1e-4
    decay_rate = 0.99

    train_op = tf.train.RMSPropOptimizer(learning_rate, decay_rate).minimize(loss)

    return network,sampling_prob,states_batch_pl, actions_batch_pl, discount_rewards_batch_pl , loss , train_op 
     

if __name__ == '__main__':
    
    env = gym.make("Pong-v0")
    observation = env.reset()

    prev_x = None
    running_reward = None
    reward_sum = 0
    episode_number = 0

    xs, ys, rs = [], [], []

    # calculate size 
    D = prepro( observation ).shape

    # policy network
    network,sampling_prob,states_batch_pl, actions_batch_pl, discount_rewards_batch_pl , loss , train_op   = createNN()

    with tf.Session() as sess:
        tl.layers.initialize_global_variables(sess)

        if resume and os.path.exists( model_file_name  )  :
            load_params = tl.files.load_npz(name=model_file_name)
            tl.files.assign_params(sess, load_params, network)

        sess.graph.finalize()  # Graph is read-only after this statement.

        network.print_params()
        network.print_layers()
        
        start_time = time.time()
        game_number = 0  # turns count 
        
        while True:
            if render: env.render()    
            
            cur_x = prepro(observation)
            x = cur_x - prev_x if prev_x is not None else np.zeros( cur_x.shape )
            x = x.reshape( ( 1, D[0],D[1],1)  ).astype( np.float32  ) 
            prev_x = cur_x
             
            # forward feed
            # print D , x.dtype, x.shape , np.asarray( [x] ).shape
            # dp_dict = tl.utils.dict_to_one( network.all_drop )
            feed_dict={states_batch_pl: x  }
            # feed_dict.update( dp_dict )

            prob = sess.run(
                sampling_prob,
                feed_dict=feed_dict 
            )
            
            # sample action according to NN output
            # action. 1: STOP  2: UP  3: DOWN
            action = np.random.choice([1,2,3], p=prob.flatten())

            observation, reward, done, _ = env.step(action)
            reward_sum += reward

            xs.append(x)            # all observations in a episode
            # cross_entropy need label start from 0 ?
            ys.append(action - 1)   # all fake labels in a episode (action begins from 1, so minus 1)
            rs.append(reward)       # all rewards in a episode

            # now all (s,a,r) are stored 

            if done: 
                episode_number += 1
                game_number = 0

                # if episode_number % batch_size == 0:
                if len(rs) > 10000 :
                    epx = np.vstack(xs)
                    epy = np.asarray(ys)
                    epr = np.asarray(rs)
                    xs, ys, rs = [], [], []

                    print('batch over...... updating parameters......' , epr.size , "samples "  )

                    # provide by tensorlayer, calculate accumulative reward 
                    disR = tl.rein.discount_episode_rewards(epr, gamma)
                    # standardize the rewards to be unit normal (helps control the gradient estimator variance) 
                    disR -= np.mean(disR) 
                    disR /= np.std(disR) 
                    
                    # train to minimize loss
                    feed_dict = {
                        states_batch_pl: epx,
                        actions_batch_pl: epy,
                        discount_rewards_batch_pl: disR
                    }
                    # feed_dict.update( network.all_drop )
                    sess.run(
                        train_op,
                        feed_dict=feed_dict
                    ) 

                    epx = epy = epr = disR = None
                    gc.collect()

                if episode_number % (batch_size * 100) == 0:
                    tl.files.save_npz(network.all_params, name=model_file_name)

                running_reward = reward_sum if running_reward is None else running_reward * 0.99 + reward_sum * 0.01
                print('resetting env. episode reward total was %f. running mean: %f' % (reward_sum, running_reward))
                reward_sum = 0
                # reset env
                observation = env.reset() # reset env
                prev_x = None

            if reward != 0:
                print(('episode %d: game %d took %.5fs, reward: %f' %
                            (episode_number, game_number,
                             time.time()-start_time, reward)),
                        ('' if reward == -1 else ' !!!!!!!!'))
                start_time = time.time()
                game_number += 1        

