P || C_max
=====================================================

Heuristic approaches to solve the NP-hard P || C_max scheduling problem.

**********************************

Partially based on [Minimizing the Makespan in Nonpreemptive Parallel Machine Scheduling Problem](https://www.researchgate.net/publication/220121254_Minimizing_the_Makespan_in_Nonpreemptive_Parallel_Machine_Scheduling_Problem) by Giampiero Chiaselotti et al.

### DEPENDENCIES
- **CPLEX (latest - academic license)**

### BUILD PROCESS (IntelliJ IDEA)
```
Build -> Build Artifacts -> MakespanMinimizer.jar
```

### RUN .jar and dynamically link CPLEX
```
$ java -jar -Djava.library.path="/opt/ibm/ILOG/CPLEX_Studio1210/opl/bin/x86-64_linux/" MakespanMinimizer.jar res/instances/S0/PCMAX_NU_1_0010_05_0.txt res/solutions 8 180 995
```
