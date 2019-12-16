library(ggplot2)

input <- read.csv(file = "res/solutions/solutions.csv", header = TRUE, sep = ",")
solverEntries <- subset(input, solver == "LPT" | solver == "SPS")

plotPointsPre <- ggplot(data = solverEntries, aes(x = obj, y = instance, color = solver, group = solver))
scaledPlot <- plotPointsPre + geom_point() + xlab("costs") + ylab("instance")
finalPlot <- scaledPlot #+ scale_color_manual(objues=c("#fa9f27", "#5428ff", "#f5503b", "#28bd5a"))

ggsave(finalPlot, file = "solver_instance_cost.png", width=6, height=4)

LPTData <- subset(input, solver == "LPT")
LPTCosts <- subset(LPTData, select = c(obj))
paste("avg costs of LPT: ", round(mean(as.numeric(as.character(LPTCosts[["obj"]]))), digits = 2))

SPSData <- subset(input, solver == "SPS")
SPSCosts <- subset(SPSData, select = c(obj))
paste("avg costs of SPS: ", round(mean(as.numeric(as.character(SPSCosts[["obj"]]))), digits = 2))
