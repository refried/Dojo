'''
Created on Jun 13, 2013

@author: hsahni3
'''
from sympy.matrices.matrices import matrix_multiply

dataPath = "dim2.txt"

from input import DataSet
from output import Plotter
import numpy as np
from scipy.cluster.vq import kmeans2, whiten

def normalDist(x, k, mu, sigma):
    print 'x',x
    print 'k',k
    print 'mu', mu
    print 'sigma', sigma
    
    print 'exp=',-(matrix_multiply(matrix_multiply((x - mu),np.linalg.inv(sigma)),np.transpose((x - mu))))/2
    to_return =  pow((2*np.pi*np.linalg.det(sigma)), -k/2)*np.exp(-(x - mu)*np.linalg.inv(sigma)*np.transpose((x - mu))/2)
    print to_return
    return to_return

if __name__ == '__main__':
    ##### Parsing data #####
    data_set = DataSet()
    data_set.parse(dataPath)
    
    data = data_set.data # N x 2
    N = data_set.num_points
    dims = data_set.dims
    
    
    ######################### begin here #############################
    numClusters = 4
    tau = np.zeros((N,numClusters))
    pi = np.ones((1, numClusters))*1/numClusters
    #mu = np.random.rand(numClusters, dims)
    random_indices = np.random.randint(0, N, numClusters)
    mu = data[random_indices][:]
    
    sigma = numClusters*[np.eye(dims)]
    
    print 'random_indices', random_indices
    
    print "tau"
    print tau
    
    print "pi"
    print pi
    
    print "mu"
    print mu
    
    print 'sigma', sigma
    
    
    # Expectation 
    for pt_no in range(N):
        for cluster_no in range(N):
            
            tau[pt_no][cluster_no] = pi[cluster_no] * normalDist(data[pt_no][:], dims, mu[cluster_no][:], sigma[cluster_no])          
    # Compute responsibility score
  #  for datapoint in data:
  #      for clusterID in range(numClusters):
   #         tau[n][numClusters] = pi*normalDist(datapoint, dims, mu, sigma)
    
    # Maximization



    
    
    
    
    
    
    
    
    
    
    
    
    ######################### example of k means ########################
    w_data = whiten(data)
    result = kmeans2(data, 10, minit = 'random')
    print result[1]
    plt = Plotter()
    plt.plot(data_set, result[1])



def evalNormal(obs, mean, covariance):
    pass
    ##### available functions in numpy:
    # numpy.pi
    # numpy.dot()
    # numpy.sqrt()
    # numpy.exp()