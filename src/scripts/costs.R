library(ggplot2)

input <- read.csv(file = "../../res/solutions/S60_solutions.csv", header = TRUE, sep = ",")
solverEntries <- subset(input, solver == "LPT" | solver == "SPS" | solver == "CPLEX" | solver == "TS")

plotPointsPre <- ggplot(data = solverEntries, aes(x = obj, y = instance, color = solver, group = solver))
finalPlot <- plotPointsPre + geom_point() + xlab("costs") + ylab("instance")

ggsave(finalPlot, file = "solver_instance_cost.png", width=6, height=4)

LPTData <- subset(input, solver == "LPT")
LPTCosts <- subset(LPTData, select = c(obj))
paste("avg costs of LPT: ", round(mean(as.numeric(as.character(LPTCosts[["obj"]]))), digits = 2))

SPSData <- subset(input, solver == "SPS")
SPSCosts <- subset(SPSData, select = c(obj))
paste("avg costs of SPS: ", round(mean(as.numeric(as.character(SPSCosts[["obj"]]))), digits = 2))

CPLEXData <- subset(input, solver == "SPS")
CPLEXCosts <- subset(CPLEXData, select = c(obj))
paste("avg costs of CPLEX: ", round(mean(as.numeric(as.character(CPLEXCosts[["obj"]]))), digits = 2))

TSData <- subset(input, solver == "TS")
TSCosts <- subset(TSData, select = c(obj))
paste("avg costs of TS: ", round(mean(as.numeric(as.character(TSCosts[["obj"]]))), digits = 2))
