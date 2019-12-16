library(ggplot2)
library(plyr)

input <- read.csv(file = "res/solutions/solutions.csv", header = TRUE, sep = ",")

solverEntries <- subset(input, solver == "LPT" | solver == "SPS")
plotPointsPre <- ggplot(data = solverEntries, aes(x = as.numeric(as.character(runtime)), y = instance, color = solver, group = solver))
plotPointsPreScaled <- plotPointsPre + geom_point() + xlab("runtime (s)") + ylab("instance")
finalPlot <- plotPointsPreScaled #+ scale_color_manual(values=c("#fa9f27", "#5428ff", "#f5503b", "#28bd5a"))

ggsave(finalPlot, file = "solver_instance_time.png", width = 6, height = 40)

LPTData <- subset(input, solver == "LPT")
LPTRuntime <- subset(LPTData, select = c(runtime))
paste("avg runtime of LPT: ", round(mean(as.numeric(as.character(LPTRuntime[["runtime"]]))), digits = 2))

SPSData <- subset(input, solver == "SPS")
SPSRuntime <- subset(SPSData, select = c(runtime))
paste("avg runtime of SPS: ", round(mean(as.numeric(as.character(SPSRuntime[["runtime"]]))), digits = 2))
