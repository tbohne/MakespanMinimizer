import ilog.concert.*;
import ilog.cplex.IloCplex;

import java.util.ArrayList;
import java.util.List;

public class MIPFormulation {

    private final Instance instance;
    private final double timeLimit;
    private final boolean hideCPLEXOutput;
    private final int mipEmphasis;
    private final double tolerance;

    /**
     * Constructor
     *
     * @param instance        - instance to be solved
     * @param timeLimit       - time limit for the solving procedure
     * @param hideCPLEXOutput - determines whether the CPLEX output gets hidden
     * @param mipEmphasis     - controls trade-offs between speed, feasibility and optimality
     * @param tolerance       - termination tolerance
     */
    public MIPFormulation(Instance instance, double timeLimit, boolean hideCPLEXOutput, int mipEmphasis, double tolerance) {
        this.instance = instance;
        this.timeLimit = timeLimit;
        this.hideCPLEXOutput = hideCPLEXOutput;
        this.mipEmphasis = mipEmphasis;
        this.tolerance = tolerance;
    }

    public Solution solve() {

        Solution sol = new Solution(instance);

        try {
            // define new model
            IloCplex cplex = new IloCplex();

            IloIntVar[][] x = new IloIntVar[this.instance.getNumOfMachines()][];
            this.initVariables(cplex, x);

            IloIntVar cMax = cplex.intVar(0, Integer.MAX_VALUE);
            cplex.addMinimize(cMax);

            this.addConstraints(cplex, x, cMax);

            this.setCPLEXConfig(cplex);
            double startTime = cplex.getCplexTime();

            if (cplex.solve()) {
                this.generateSolutionFromVariableAssignments(sol, x, cplex);
                sol.setTimeToSolve(cplex.getCplexTime() - startTime);
            }
            cplex.end();

        } catch (IloException e) {
            e.printStackTrace();
        }
        return sol;
    }

    private void generateSolutionFromVariableAssignments(Solution sol, IloIntVar[][] x, IloCplex cplex) throws ilog.concert.IloException {
        List<Machine> machines = new ArrayList<>();
        for (int i = 0; i < this.instance.getNumOfMachines(); i++) {
            machines.add(new Machine());
        }
        for (int i = 0; i < x.length; i++) {
            for (int q = 0; q < x[0].length; q++) {
                if (Math.round(cplex.getValue(x[i][q])) == 1) {
                    machines.get(i).setJob(this.instance.getProcessingTimes().get(q));
                }
            }
        }
        sol.setMachineAllocations(machines);
    }

    private void initVariables(IloCplex cplex, IloIntVar[][] x) throws ilog.concert.IloException {
        for (int i = 0; i < this.instance.getNumOfMachines(); i++) {
            x[i] = cplex.intVarArray(this.instance.getNumOfJobs(), 0, 1);
        }
    }

    private void addConstraints(IloCplex cplex, IloIntVar[][] x, IloIntVar cMax ) throws ilog.concert.IloException {

        // --- Constraint (1) ---
        for (int j = 0; j < this.instance.getNumOfJobs(); j++) {
            IloLinearIntExpr expr = cplex.linearIntExpr();
            for (int i = 0; i < this.instance.getNumOfMachines(); i++) {
                expr.addTerm(1, x[i][j]);
            }
            cplex.addEq(expr, 1);
        }

        // --- Constraint (2) ---
        for (int i = 0; i < this.instance.getNumOfMachines(); i++) {
            IloLinearIntExpr expr = cplex.linearIntExpr();
            for (int j = 0; j < this.instance.getNumOfJobs(); j++) {
                expr.addTerm(this.instance.getProcessingTimes().get(j), x[i][j]);
            }
            cplex.addGe(cplex.diff(cMax, expr), 0);
        }
    }

    private void setCPLEXConfig(IloCplex cplex) throws ilog.concert.IloException {

        if (this.hideCPLEXOutput) {
            cplex.setOut(null);
        }

        // set time limit
        cplex.setParam(IloCplex.Param.TimeLimit, this.timeLimit);

        // control trade-offs between speed, feasibility and optimality
        cplex.setParam(IloCplex.IntParam.MIPEmphasis, this.mipEmphasis);

        // set termination tolerance
        cplex.setParam(IloCplex.DoubleParam.EpAGap, this.tolerance);
        cplex.setParam(IloCplex.DoubleParam.EpGap, this.tolerance);
    }
}
