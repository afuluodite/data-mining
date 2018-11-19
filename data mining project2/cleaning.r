input=read.csv('train.csv',, stringsAsFactors=FALSE)
full=input[c(1:1000),]


#exchange the time into days
# Get the time value:
full$TimeValue <- sapply(full$AgeuponOutcome,  
                         function(x) strsplit(x, split = ' ')[[1]][1])

# Now get the unit of time:
full$UnitofTime <- sapply(full$AgeuponOutcome,  
                          function(x) strsplit(x, split = ' ')[[1]][2])

# Fortunately any "s" marks the plural, so we can just pull them all out
full$UnitofTime <- gsub('s', '', full$UnitofTime)

full$TimeValue  <- as.numeric(full$TimeValue)
full$UnitofTime <- as.factor(full$UnitofTime)
# Make a multiplier vector
multiplier <- ifelse(full$UnitofTime == 'day', 1,
                     ifelse(full$UnitofTime == 'week', 7,
                            ifelse(full$UnitofTime == 'month', 30, # Close enough
                                   ifelse(full$UnitofTime == 'year', 365, NA))))

# Apply our multiplier
full$AgeinDays <- full$TimeValue  * multiplier





# Replace blank sex with most common
full$SexuponOutcome <- ifelse(nchar(full$SexuponOutcome)==0, 
                              'Spayed Female', full$SexuponOutcome)



# Use "grepl" to look for "Mix"
full$IsMix <- ifelse(grepl('Mix', full$Breed), 1, 0)

# Split on "/" and remove " Mix" to simplify Breed
full$SimpleBreed <- sapply(full$Breed, 
                           function(x) gsub(' Mix', '', 
                                            strsplit(x, split = '/')[[1]][1]))

# Use strsplit to grab the first color
full$SimpleColor <- sapply(full$Color, 
                           function(x) strsplit(x, split = '/| ')[[1]][1])


#convert simpleinput into factor
simpleinput=full[,c(1,2,3,10,12,13,7)]
simpleinput=full[,c(2,3,10,12,13,7)]
for(i in length(simpleinput)){
  simpleinput[,i]=as.factor(simpleinput[,i])
}


library(C50)
mod2 <- C5.0(OutcomeType ~ ., data = simpleinput)
plot(mod2)
plot(mod2,subtree=2)




x=read.csv("play_tennis.csv",sep=",",header=T)
w=x[,c(2,4,6)]
mod1 <- C5.0(PlayTennis ~ ., data = w)
plot(mod1)
plot(mod1,subtree=3)





#choose some breed  
simple=simpleinput[which(simpleinput$SimpleBreed=='Siamese'|simpleinput$SimpleBreed=='Siberian Husky'
                  |simpleinput$SimpleBreed=='Yorkshire Terrier'|simpleinput$SimpleBreed=='Pit Bull'
                  |simpleinput$SimpleBreed=='German Shepherd'|simpleinput$SimpleBreed=='Domestic Shorthair'
                  |simpleinput$SimpleBreed=='Domestic Medium Hair'|simpleinput$SimpleBreed=='Domestic Longhair'
                  |simpleinput$SimpleBreed=='Chihuahua Longhair'),]
simplebread=simple[-which(simple$OutcomeType=='Died'),]

  
#color
color=simplebread[which(simplebread$SimpleColor=='Black'|simplebread$SimpleColor=='Brown'
                        |simplebread$SimpleColor=='Blue'|simplebread$SimpleColor=='White'
                        |simplebread$SimpleColor=='Orange'),]


#generate my rule
transfer=color[which(color$OutcomeType=='Transfer'),]   
return=color[which(color$OutcomeType=='Return_to_owner'),]
euthanasia= color[which(color$OutcomeType=='Euthanasia'),]
adoption=color[which(color$OutcomeType=='Adoption'),]


#result
write.csv(simpleinput[-1,],'output.csv')
write.csv(color[-1,],'output1.csv')

######################
ssinput=read.csv('in.csv',, stringsAsFactors=FALSE)
for(i in length(ssinput)){
  ssinput[,i]=as.factor(ssinput[,i])
}
mod3 <- C5.0(OutcomeType ~ ., data = ssinput)
plot(mod3)
plot(mod3,subtree=2)


install.packages("rpart")
library(rpart)
fit = rpart(OutcomeType ~ ., data = ssinput)
plot(fit)
text(fit,use.n=TRUE,cex=1)
fit

