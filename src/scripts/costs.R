library(ggplot2)

input <- read.csv(file = "../../res/solutions/EVAL_solutions.csv", header = TRUE, sep = ",")

solverEntries <- subset(input, solver == "LPT" | solver == "SPS" | solver == "CPLEX" | solver == "TS")
plotPointsPre <- ggplot(data = solverEntries, aes(x = obj, y = instance, color = solver, group = solver))
finalPlot <- plotPointsPre + geom_point() + xlab("costs") + ylab("instance")
ggsave(finalPlot, file = "solver_instance_cost.png", width=6, height=4)

compute_avg_costs <- function(s) {
    data <- subset(input, solver == s)
    costs <- subset(data, select = c(obj))
    return(round(mean(as.numeric(as.character(costs[["obj"]]))), digits = 2))
}

paste("avg costs of LPT: ", compute_avg_costs("LPT"))
paste("avg costs of SPS: ", compute_avg_costs("SPS"))
paste("avg costs of CPLEX: ", compute_avg_costs("CPLEX"))
paste("avg costs of TS: ", compute_avg_costs("TS"))
