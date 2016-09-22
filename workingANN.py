import matplotlib.pyplot as plt
import numpy as np
np.random.seed(42)

def sigmoid(x):
    return 1 / (1 + np.exp(-x))

def dsigmoid(x):
    return x * (1 - x)

class ANN:

    def __init__(self, size, size2,learning_rate=0.1):
        self.size = size
        #weights=> an array of size with random values in each slot from 0 to 1
        self.weights = np.random.rand(size, size2)
        self.weights2 = np.random.rand(size2)

        self.learning_rate = learning_rate
        self.incoming=[]
        self.outputs=[]

    def make_layers(self, num,nodes):
        '''This function makes multiple hidden layers'''
        self.weights = np.random.rand(nodes, nodes)
        if num>1:
            for i in range(0,num):
                self.hidden_weights_list.append(weights)

    def forward(self, X):
        '''This funciton propagate forward.'''
        #forward form input to hidden layer
        self.incoming.append(X)
        act = X.dot(self.weights)
        act = sigmoid(act)
        #forward from hidden to output layer
        self.incoming.append(act)
        self.outputs.append(act)
        act= act.dot(self.weights2)
        act=sigmoid(act)
        self.outputs.append(act)
        self.input=X
        return act
    def back(self,y, previous_err):
        '''This function back propagate from hidden layer to input.'''
        out=self.outputs.pop()
        size=len(previous_err)
        previous_err=previous_err.reshape(size,1)
        #use previosu error to calculate delta
        delta= previous_err.dot(self.weights2.reshape(len(self.weights2),1).T)
        err= dsigmoid(out)* delta
        tempt=self.incoming.pop()
        update= tempt.T.dot(err)
        self.weights += self.learning_rate * update
        return update
    def backward(self, err,y, outputs):
        '''This function back propagate from output to hidden layer.'''
        err = err * dsigmoid(outputs)
        update = self.incoming.pop().T.dot(err)
        self.weights2 += self.learning_rate * update
        #calls back to keep propagating downward
        self.back(y, err)
        return update

    def reportAccuracy(self, X, y):
        '''This function reports the accuracy'''
        out = self.forward(X)
        out = np.round(out)
        count = np.count_nonzero(y - out)
        correct = len(X) - count
        print "%.4f" % (float(correct)*100.0 / len(X))

    def calculateDerivError(self, y, pred):
        '''This function calculate the derivative of error.'''
        return 2*(y - pred)


    def calculateError(self, y, pred):
        '''This function calculates the error.'''
        return (np.sum(np.power((y - pred), 2)))

    def iteration(self, X, y):
        '''This function propagate foward and backward once.'''
        self.forward(X)
        out= self.outputs.pop()
        err = self.calculateError(y, out)
        deriv_err = self.calculateDerivError(y, out)
        self.backward(deriv_err,y,out)

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
    '''This function checks the gradient.'''
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

    model = ANN(10, 25,0.001)
    model.train(X, y, 20000)
