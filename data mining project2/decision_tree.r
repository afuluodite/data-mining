#read input and covert to factor
ssinput=read.csv('in.csv',, stringsAsFactors=FALSE)
for(i in length(ssinput)){
  ssinput[,i]=as.factor(ssinput[,i])
}


#c5.0
library(C50)
mod3 <- C5.0(OutcomeType ~ ., data = ssinput)
plot(mod3)
plot(mod3,subtree=2)



#rpart
install.packages("rpart")
library(rpart)
fit = rpart(OutcomeType ~ ., data = ssinput)
plot(fit)
text(fit,use.n=TRUE,cex=1)
fit
