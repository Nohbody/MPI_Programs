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
    // Map team name (string) to number
    private ST<String, Integer> team2Num;
    // structure with records of team info
    private TeamInfo[] teamInfo;
    // matrix of remain games between teams
    private int[][] remainingGames;
    private boolean isEliminated;
    private SET<String> certificateOfElimination;

    private class TeamInfo {
        private String name;
        private int wins;
        private int losses;
        private int remaining;
    }

    // create a baseball division from given filename in format specified below
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
    }

    public int numberOfTeams() {
        return numberOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return team2Num;
    }

    // number of wins for given team
    public int wins(String team) {
        if (!team2Num.contains(team)) throw new java.lang.IllegalArgumentException("No such team");
        return teamInfo[team2Num.get(team)].wins;
    }

    // number of losses for given team
    public int losses(String team) {
        if (!team2Num.contains(team)) throw new java.lang.IllegalArgumentException("No such team");
        return teamInfo[team2Num.get(team)].losses;
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!team2Num.contains(team)) throw new java.lang.IllegalArgumentException("No such team");
        return teamInfo[team2Num.get(team)].remaining;
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!team2Num.contains(team1) || !team2Num.contains(team2))
            throw new java.lang.IllegalArgumentException("No such team");

        // Use numbers in team2Num as remainingGames indexes
        return remainingGames[team2Num.get(team1)][team2Num.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team)
    {
        if (!team2Num.contains(team)) throw new java.lang.IllegalArgumentException("No such team");

        calcElimination(team);
        return isEliminated;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!team2Num.contains(team)) throw new java.lang.IllegalArgumentException("No such team");

        if (isEliminated) return certificateOfElimination;
        else return null;
    }

    // Calculate whether team X is eliminated with trivial and non-trivial method
    private void calcElimination(String team) {
        isEliminated = false;
        certificateOfElimination = new SET<String>();
        int teamX, maxTeamX;

        // According number of teamX
        teamX = team2Num.get(team);
        // Max winning Team X can have
        maxTeamX = teamInfo[teamX].wins + teamInfo[teamX].remaining;

        // Trivial method: Check if max winning of teamX is smaller than the already won number of some team
        for (int i = 0; i < numberOfTeams; i++)
            if (i != teamX && teamInfo[i].wins > maxTeamX) {
                isEliminated = true;
                certificateOfElimination.add(teamInfo[i].name);
                break;
            }

        // Non-trivial method. Max-flow
        if (!isEliminated) {
            FlowNetwork network;
            FlowEdge edge;
            int totalRemainCombos = 0;
            int vertices, source, sink, match;

            // Calculate the number of different combination of matches;
            for (int i = 0; i < numberOfTeams - 1; i++) {
                for (int j = i + 1; j < numberOfTeams; j++) {
                    if (i != teamX && j != teamX && remainingGames[i][j] != 0) {
                        totalRemainCombos++;
                    }
                }
            }

            // Get total vertices number and generate flow network
            vertices = totalRemainCombos + numberOfTeams + 2;
            network = new FlowNetwork(vertices);
            source = vertices - 2;
            sink = vertices - 1;
            match = numberOfTeams;

            // Add edges to the flow network
            for (int i = 0; i < numberOfTeams; i++) {
                if (maxTeamX - teamInfo[i].wins >= 0) {
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

            // Determine if teamX is eliminated || All edge from source to match combo is full
            for (int i = numberOfTeams; i < numberOfTeams + totalRemainCombos; i++)
                // Edge not full
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
