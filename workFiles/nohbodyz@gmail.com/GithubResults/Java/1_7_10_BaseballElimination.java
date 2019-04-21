/**
 * Created by zora on 4/7/16.
 */
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

public class BaseballElimination {
    private static final double INF = Double.POSITIVE_INFINITY;
    private int numberOfTeams;
    private ST<String, Integer> team2Num;
    private TeamInfo[] teamInfo;
    private int[][] remainingGames;
    private boolean isEliminated;
    private SET<String> certificateOfElimination;

    private class TeamInfo {
        private String name;
        private int wins;
        private int losses;
        private int remaining;
    }

    public BaseballElimination(String filename) {
        In in = new In(filename);
        numberOfTeams = in.readInt();
        team2Num = new ST<String, Integer>();
        teamInfo = new TeamInfo[numberOfTeams];
        remainingGames = new int[numberOfTeams][numberOfTeams];

        int counter = 0;
        String name = "";
        while (in.hasNextLine()) {
            try {
                name = in.readString();
            }
            catch (NoSuchElementException err) { break; }

            team2Num.put(name, counter);
            teamInfo[counter] = new TeamInfo();
            teamInfo[counter].name = name;
            teamInfo[counter].wins = in.readInt();
            teamInfo[counter].losses = in.readInt();
            teamInfo[counter].remaining = in.readInt();
            for (int i = 0; i < numberOfTeams; i++)
                remainingGames[counter][i] = in.readInt();
            counter++;
        }
    }                   // create a baseball division from given filename in format specified below
    public int numberOfTeams() {
        return numberOfTeams;
    }                       // number of teams

    public Iterable<String> teams() {
        return team2Num;
    }                               // all teams
    public int wins(String team) {
        if (!team2Num.contains(team)) throw new java.lang.IllegalArgumentException("No such team");
        return teamInfo[team2Num.get(team)].wins;
    }                     // number of wins for given team

    public int losses(String team) {
        if (!team2Num.contains(team)) throw new java.lang.IllegalArgumentException("No such team");
        return teamInfo[team2Num.get(team)].losses;
    }                    // number of losses for given team

    public int remaining(String team) {
        if (!team2Num.contains(team)) throw new java.lang.IllegalArgumentException("No such team");
        return teamInfo[team2Num.get(team)].remaining;
    }                // number of remaining games for given team

    public int against(String team1, String team2) {
        if (!team2Num.contains(team1) || !team2Num.contains(team2))
            throw new java.lang.IllegalArgumentException("No such team");
        return remainingGames[team2Num.get(team1)][team2Num.get(team2)];
    }   // number of remaining games between team1 and team2

    public boolean isEliminated(String team)
    {
        if (!team2Num.contains(team)) throw new java.lang.IllegalArgumentException("No such team");
        calcElimination(team);
        return isEliminated;
    }// is given team eliminated?

    public Iterable<String> certificateOfElimination(String team) {
        if (!team2Num.contains(team)) throw new java.lang.IllegalArgumentException("No such team");
        calcElimination(team);
        return certificateOfElimination;
    } // subset R of teams that eliminates given team; null if not eliminated

    private void calcElimination(String team) {
        isEliminated = false;
        certificateOfElimination = new SET<String>(null);
        int teamX, maxTeamX;
        teamX = team2Num.get(team);
        maxTeamX = teamInfo[teamX].wins + teamInfo[teamX].remaining;
        if (teamInfo[0].wins > maxTeamX) {
            isEliminated = true;
            certificateOfElimination.add(teamInfo[0].name);
        }
        else {
            FlowNetwork network;
            FlowEdge edge;
            int totalRemainMatches = 0;
            int vertices, source, sink, match;

            teamX = team2Num.get(team);
            maxTeamX = teamInfo[teamX].wins + teamInfo[teamX].remaining;
            for (int i = 0; i < numberOfTeams - 1; i++) {
                for (int j = i + 1; j < numberOfTeams; j++) {
                    if (i != teamX && j != teamX && remainingGames[i][j] != 0) {
                        totalRemainMatches++;
                    }
                }
            }
            vertices = totalRemainMatches + numberOfTeams + 2;
            network = new FlowNetwork(vertices);
            source = vertices - 2;
            sink = vertices - 1;
            match = numberOfTeams;

            for (int i = 0; i < numberOfTeams; i++) {
                if (maxTeamX - teamInfo[i].wins >= 0){
                    edge = new FlowEdge(i, sink, maxTeamX - teamInfo[i].wins);
                    network.addEdge(edge);
                }
                for (int j = i + 1; j < numberOfTeams; j++) {
                    if (i != teamX && j != teamX && remainingGames[i][j] != 0) {
                        edge = new FlowEdge(source, match, remainingGames[i][j]);
                        network.addEdge(edge);
                        edge = new FlowEdge(match, i, INF);
                        network.addEdge(edge);
                        edge = new FlowEdge(match, j, INF);
                        network.addEdge(edge);
                        match++;
                    }
                }
            }

            FordFulkerson maxFlow = new FordFulkerson(network, source, sink);
            //StdOut.println(network.toString());
            //StdOut.println(maxFlow.value());

            for (int i = numberOfTeams; i < numberOfTeams + totalRemainMatches; i++)
            {
                if (maxFlow.inCut(i)) {
                    isEliminated = true;
                    for (FlowEdge iterEdge : network.adj(i))
                        if (iterEdge.to() != i) {
                            String tempName = teamInfo[iterEdge.to()].name;
                            if (!certificateOfElimination.contains(tempName))
                                certificateOfElimination.add(tempName);
                        }
                }
            }
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
