import matplotlib.pyplot as plt
import numpy as np
np.random.seed(42)

def sigmoid(x):
    return 1 / (1 + np.exp(-x))

def dsigmoid(x):
    return x * (1 - x)

class Perceptron:

    def __init__(self, size, size2,learning_rate=0.1):
        self.size = size
        #weights=> an array of size with random values in each slot from 0 to 1
        self.weights = np.random.rand(size, size2)
        self.weights2 = np.random.rand(size2)

        self.learning_rate = learning_rate
        self.incoming=[]
        self.outputs=[]
        self.hidden_weights_list=[]
    def make_layers(self, num,nodes):
        self.hidden_layers=nodes
        if num>1:
            for i in range(0,num-1):
                weights = np.random.rand(nodes, nodes)
                self.hidden_weights_list.append(weights)
        self.num_weights=len(self.hidden_weights_list)

    def forward(self, X):
        #going up from input nodes
        self.incoming.append(X)
        act = X.dot(self.weights)
        act = sigmoid(act)
        self.outputs.append(act)
        #going up from hidden node to another hidden layer
        for i in range(0,self.hidden_layers-1):
            self.incoming.append(act)
            print act.shape
            print self.hidden_weights_list[i].shape
            act= act.dot(self.hidden_weights_list[i])
            act=sigmoid(act)
            self.outputs.append(act)
        #going up from hidden node to output node
        self.incoming.append(act)
        act= act.dot(self.weights2)
        act=sigmoid(act)
        self.outputs.append(act)
        self.input=X
        return act
    def back(self,y, previous_err):
        out=self.outputs.pop()
        previous_err=previous_err.reshape(683,1)
        delta= previous_err.dot(self.weights2.reshape(25,1).T)
        err= dsigmoid(out)* delta
        tempt=self.incoming.pop()
        update= tempt.T.dot(err)
        self.hidden_weights_list[self.num_weights]+= self.learning_rate * update
        self.hidden_back(y, err, self.num_weights)
        return update
    def hidden_back(self,y, previous_err, index):
        out=self.outputs.pop()
        #previous_err=previous_err.reshape(683,1)
        delta= previous_err.dot(self.hidden_weights_list[index])
        err= dsigmoid(out)* delta
        tempt=self.incoming.pop()
        update= tempt.T.dot(err)
        self.hidden_weights_list[index] += self.learning_rate * update
        return update
    def backward(self, err,y, outputs):
        err = err * dsigmoid(outputs)
        update = self.incoming.pop().T.dot(err)
        self.weights2 += self.learning_rate * update
        self.back(y, err)
        return update

    def reportAccuracy(self, X, y):
        out = self.forward(X)
        out = np.round(out)
        count = np.count_nonzero(y - out)
        correct = len(X) - count
        print "%.4f" % (float(correct)*100.0 / len(X))

    def calculateDerivError(self, y, pred):
        #print y.shape, " shape of y in deriveerror"
        #print pred.shape, " shape of pred in deriveerror"
        return 2*(y - pred)


    def calculateError(self, y, pred):
        #print y.shape, "y shapes"
        return (np.sum(np.power((y - pred), 2)))

    def iteration(self, X, y):
        self.make_layers(3,25)
        self.forward(X)
        #print out.shape, " first layer output shapes"
        #out=self.forward(out)
        #return a vector of outputs,
        #one for every ten inputs
        out= self.outputs.pop()
        err = self.calculateError(y, out)
        #print err.shape, "testing"
        #returns a scalar for the error
        # print err
        #calculate difference between target vs generate outut
        deriv_err = self.calculateDerivError(y, out)

        self.backward(deriv_err,y,out)
        #self.back(y)

    def train(self, X, y, number_epochs):
        for i in range(number_epochs):
            self.iteration(X, y)
            self.reportAccuracy(X, y)


def loadDataset(filename='breast_cancer.csv'):
    my_data = np.genfromtxt(filename, delimiter=',', skip_header=1)

    # The labels of the cases
    # Raw labels are either 4 (cancer) or 2 (no cancer)
    # Normalize these classes to 0/1
    y = (my_data[:, 10] / 2) - 1

    # Case features
    X = my_data[:, :10]

    # Normalize the features to (0, 1)
    X_norm = X / X.max(axis=0)

    return X_norm, y



def gradientChecker(model, X, y):
    epsilon = 1E-5

    model.weights[3] += epsilon
    #model.weights2[1] += epsilon
    out1 = model.forward(X)
    err1 = model.calculateError(y, out1)

    model.weights[3] -= 2*epsilon
    #model.weights2[1] -= 2*epsilon
    out2 = model.forward(X)
    err2 = model.calculateError(y, out2)

    numeric = (err2 - err1) / (2*epsilon)
    print numeric

    model.weights[3] += epsilon
    #model.weights2[1] += epsilon
    out3 = model.forward(X)
    err3 = model.calculateDerivError(y, out3)
    derivs = model.backward(err3,y,model.outputs.pop())
    print derivs[3]




if __name__=="__main__":
    X, y = loadDataset()
    # X = X
    #print X.shape

    model = Perceptron(10, 25,0.001)
    model.train(X, y, 20000)
    #print X
    #print y
    #print X.shape, y.shape
    #gradientChecker(model, X, y)
    #print model.size, " size"
