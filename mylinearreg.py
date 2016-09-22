#import matplotlib.pyplot as plt
import numpy as np
np.random.seed(42) # Get the same random numbers every time


# Ugly code for thinking about linear regression with gradient descent

################################################################
### Load the dataset


# def log_in(server,username, password):
#     '''This function checks to see if the password matches'''
#     check=server.get(username)
#     if check==password:
#         return True
#     else:
#         return False
#makes a new account



################################################################
### Init the model parameters


def domath(rate, closed_value, record_high,iterations):
    '''This function does the math for the linear regression.'''
    weight = np.random.rand(1)
    bias = np.random.rand(1)
    init_cost = np.sum(np.power((record_high*weight+bias) - closed_value, 2))
    print init_cost, " initial cost"
    #loop through all the iteration to update the weights, error, and bias
    for i in range(iterations):
        error = (record_high*weight+bias) - closed_value
        weight = weight - np.sum((rate * error * record_high) / len(record_high))
        bias = bias - np.sum((rate * error * 1.0 )/ len(record_high))
        end_cost = np.sum(np.power((record_high*weight+bias) - closed_value, 2))
    end_end_cost = np.sum(np.power((record_high*weight+bias) - closed_value, 2))
    print end_end_cost ," end end cost"
if __name__ == "__main__":
    my_data = np.genfromtxt('djia_temp.csv', delimiter=';', skip_header=1)[:10]
    closed_value = my_data[:, 1]
    record_high = my_data[:, 2]
    domath(.001, closed_value,record_high, 100000)
################################################################
### How do we change the weight and the bias to make the line's fit better?





#end_cost = np.sum(np.power((hs_gpa*weight+bias) - col_gpa, 2))





################################################################
### Graph the dataset along with the line defined by the model
#
# xs = np.arange(0, 5)
# ys = xs * weight + bias
#
# plt.plot(hs_gpa, col_gpa, 'r+', xs, ys, 'g-')
# plt.show()
