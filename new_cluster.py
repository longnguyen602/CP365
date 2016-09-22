import numpy as np
import random as ran
ran.seed(99)
def loadDataset(filename='u.data'):
    my_data = np.genfromtxt(filename, delimiter=None, skip_header=0)
    return my_data
def loadmoviestitle(filename='u.item'):
    my_data = np.genfromtxt(filename, delimiter='|',dtype=None,skip_header=0)
    return my_data
class Cluster:
    def __init__(self,k,data):
        self.k=k
        self.data=data
        self.cluster={}
    def count_user(self):
        '''This method counts the number of user in the data set'''
        users={}
        counter=0
        for i in range(0, len(self.data)):
            if not users.has_key(data[i,0]):
                counter+=1
                users.update({data[i,0]:0})
        self.user_size=counter
        return counter
    def find_user(self, user_id):
        '''This method search to see if the user user_id is in the dataset'''
        for i in range(0, len(self.data)):
            if data[i,0]==user_id:
                print " Found ", user_id
                return
    def create_centroids(self):
        '''This method create k-centroids randomly intitially'''
        self.centroid_list=[]
        for k in range(0, self.k):
            centroid=[]
            for i in range(0,self.user_size):
                num=ran.uniform(1,5)
                centroid.append(num)
            self.centroid_list.append(centroid)
        #print self.centroid_list[0]
        #print len(self.centroid_list)
    def group_movies(self):
        '''This method group ratings of the same movies together.'''
        x,y=self.data.shape
        movies_list={}
        #loop through all the ratings and add the data into the dicitonary
        #using the movie id as key
        for i in range(0, x):
            current_key=self.data[i][1]
            curr_data=[self.data[i][0],self.data[i][1],self.data[i][2],self.data[i][3]]
            #append the data into the dictionary if the key exists, if not, make one
            if movies_list.has_key(current_key):
                movies_list.get(current_key).append(curr_data)
            else:
                empty_list=[]
                empty_list.append(curr_data)
                movies_list.update({current_key:empty_list})
        print len(movies_list.keys()), " number of movies"
        self.movie_size=len(movies_list.keys())
        self.my_movies_list=movies_list
        #self.print_ratings_size()
        return movies_list
    def print_ratings_size(self):
        '''This method just prints the number of ratings each movie has.'''
        keys=self.my_movies_list.keys()
        for i in range(0, self.movie_size):
            print len(self.my_movies_list.get(keys[i])), " size of each movie rating"
    def assign_vector(self):
        #10 centroids right now
        self.centroid_list
        #list of movies with all ratings/each
        self.my_movies_list
        keys=self.my_movies_list.keys()
        for i in range(0, self.movie_size):
            #rating_list=all the rating of a single movie
            rating_list=self.my_movies_list.get(keys[i])
            self.cal_distance(rating_list)
    def cal_distance(self, rating_list):
        '''This method calculates the distance between each movie and the centroid.'''
        min_distance=1000000.0
        min_index=0
        #loops through all centroids
        for i in range(0, len(self.centroid_list)):
            sum_n=0
            temp_dist=0
            #look at all the ratings for a particular movie calculate distance using ratings that exist
            for j in range(0, len(rating_list)):
                user_id=rating_list[j][0]
                rating=rating_list[j][2]
                centroid_rating=self.centroid_list[i][int(user_id)-1]
                difference_squared=np.power((rating-centroid_rating),2)
                sum_n+=difference_squared
            temp_dist=np.sqrt(sum_n)
            if temp_dist<min_distance:
                min_distance=temp_dist
                #assign min_index to the closet centroid in centroid_list
                min_index=i
        self.actual_assign(min_index,rating_list)
    def actual_assign(self, i,rating_list):
        '''This method actually assign each movie to its closest centroid.'''
        movie_id=rating_list[0][1]
        value=i
        self.cluster.update({movie_id:value})

    def cal_new_centroids(self):
        '''This method create new centroids.'''
        keys=self.cluster.keys()
        new_centroids_list=[]
        for centroid_index in range(0,self.k):
            movies_list=[]
            for i in range(0,len(keys)):
                if self.cluster.get(keys[i])==centroid_index:
                    movies_list.append(keys[i])
            new_centroid=self.redistance(movies_list, centroid_index)
            new_centroids_list.append(new_centroid)
        self.old_centroid_list=self.centroid_list
        self.centroid_list=new_centroids_list

    def redistance(self, movies_list,centroid_index):
        '''This method calculate the new values for each centroid'''
        #loop through all element of centroid vector
        new_centroid=[]
        sum_n=0.0
        total=0.0
        for j in range(0,len(movies_list)):
        #ratings=list of ratings
            ratings=self.my_movies_list.get(movies_list[j])
        #loop through all the ratings to get the corresponding rating
            average=0
            sum_n=0
            for k in range(0, len(ratings)):
                rating=ratings[k][2]
                sum_n+=rating
            average=sum_n/(float(len(ratings)))
            #print average
            total+=average
        all_movies_average=total/float(len(movies_list))
        for n in range(0,user_size):
            new_centroid.append(all_movies_average)
        return new_centroid
    def stop(self):
        run= False
        for i in range(0, self.k):
            if not self.old_centroid_list[i][0]==self.centroid_list[i][0]:
                run=True
        return run

def print_movies(cluster, centroid_index, num, movies):
    '''This method print movie titles.'''
    keys=cluster.keys()
    for i in range(0,num):
        movie_id=keys[i]
        assigned_centroid_index=cluster.get(movie_id)
        if centroid_index==assigned_centroid_index:
            print movies[movie_id][1]







if __name__=="__main__":
    #load the data set of movies and u.data
    movies=loadmoviestitle()
    data=loadDataset()
    cluster= Cluster(10, data)
    user_size=cluster.count_user()
    #intitially randomly create centroids, group ratings of same movies together for easy use later on, assign
    #each movie vector to the closest centroids , then re-calculate centroids
    cluster.create_centroids()
    cluster.group_movies()
    cluster.assign_vector()
    cluster.cal_new_centroids()
    counter=0
    #do the samething as above but only stop when the centroids no longer change
    while cluster.stop():
    #for i in range(0,5):
        counter+=1
        if counter==1:
            print counter, " Iteration"
        else:
            print counter, " Iterations"
        cluster.assign_vector()
        cluster.cal_new_centroids()
    #print out the movies in a cluster, only look at certain number of movies
    #for example, look at the first 1000 movies, and print the ones that are in
    #the same cluster
    print_movies(cluster.cluster, 9, 1000, movies)
