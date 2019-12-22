library(ggplot2)
library(plyr)

input <- read.csv(file = "res/solutions/S77_solutions.csv", header = TRUE, sep = ",")

solverEntries <- subset(input, solver == "LPT" | solver == "SPS" | solver == "CPLEX")
plotPointsPre <- ggplot(data = solverEntries, aes(x = as.numeric(as.character(runtime)), y = instance, color = solver, group = solver))
finalPlot <- plotPointsPre + geom_point() + xlab("runtime (s)") + ylab("instance")

ggsave(finalPlot, file = "solver_instance_time.png", width = 6, height = 4)

LPTData <- subset(input, solver == "LPT")
LPTRuntime <- subset(LPTData, select = c(runtime))
paste("avg runtime of LPT: ", round(mean(as.numeric(as.character(LPTRuntime[["runtime"]]))), digits = 2))

SPSData <- subset(input, solver == "SPS")
SPSRuntime <- subset(SPSData, select = c(runtime))
paste("avg runtime of SPS: ", round(mean(as.numeric(as.character(SPSRuntime[["runtime"]]))), digits = 2))

CPLEXData <- subset(input, solver == "CPLEX")
CPLEXRuntime <- subset(CPLEXData, select = c(runtime))
paste("avg runtime of CPLEX: ", round(mean(as.numeric(as.character(CPLEXRuntime[["runtime"]]))), digits = 2))
