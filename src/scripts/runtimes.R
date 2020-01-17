library(ggplot2)
library(plyr)

input <- read.csv(file = "../../res/solutions/S_TEST_solutions.csv", header = TRUE, sep = ",")

solverEntries <- subset(input, solver == "LPT" | solver == "SPS" | solver == "CPLEX" | solver == "TS")
plotPointsPre <- ggplot(data = solverEntries, aes(x = as.numeric(as.character(runtime)), y = instance, color = solver, group = solver))
finalPlot <- plotPointsPre + geom_point() + xlab("runtime (s)") + ylab("instance")
ggsave(finalPlot, file = "solver_instance_time.png", width = 6, height = 4)

compute_avg_runtime <- function(s) {
    data <- subset(input, solver == s)
    runtime <- subset(data, select = c(runtime))
    return(round(mean(as.numeric(as.character(runtime[["runtime"]]))), digits = 2))
}

paste("avg runtime of LPT: ", compute_avg_runtime("LPT"))
paste("avg runtime of SPS: ", compute_avg_runtime("SPS"))
paste("avg runtime of CPLEX: ", compute_avg_runtime("CPLEX"))
paste("avg runtime of TS: ", compute_avg_runtime("TS"))
