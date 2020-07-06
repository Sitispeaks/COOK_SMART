# Importing all necessary imports
from flask import abort
import pymongo
import json
import re
from flask import Flask
from flask import jsonify 
from bson.objectid import ObjectId
    
    
def cook_smart(request):
    """Responds to any HTTP request.
    Args:
        request (flask.Request): HTTP request object.
    Returns:
        The response text or any set of values that can be turned into a
        Response object using
        `make_response <http://flask.pocoo.org/docs/1.0/api/#flask.Flask.make_response>`.
    
    #request_json = request.get_json()
    #if request.args and 'message' in request.args:
    #    return request.args.get('message')
    #elif request_json and 'message' in request_json:
    #    return request_json['message']
    #else:
    
    """
    
    # Request files
    request_json = request.get_json()
    response = ''
    query_recipe_id = []
    
    # squares = [x**2 for x in range(10)] 
    # ingredients name to filter list maping
    
    if request_json and 'recipe_id' in request_json:
        if len(str(request_json['recipe_id'])) != 0:
            query_recipe_id = request_json['recipe_id']
    
    # Mongo Query Setup
    # mongodb+srv://<username>:<password>@cooksmart-nsxth.mongodb.net/test?retryWrites=true&w=majority
    myclient = pymongo.MongoClient("mongodb+srv://tapan:root@cooksmart-nsxth.mongodb.net/test?retryWrites=true&w=majority")
    mydb = myclient["cook_smart"]
    mycol = mydb["all_indian_recipe"]
    
    # Sample query
    # { $and:[ {"recipe_ingredient.ingr": /chicken/}, {"recipe_ingredient.ingr": /butter/} ] }
    
    result =  mycol.find({"_id": ObjectId(query_recipe_id)})
    
    result_arr = []

    for obj in result:
        result_arr.append(obj)
    
    all_obj_data = "{}".format(result_arr)
    all_obj_data = all_obj_data.replace("'",'"')
    all_obj_data = all_obj_data.replace('"url": " "}]','"url": ""}]')
    all_obj_data = all_obj_data.replace('" "','"')
    all_obj_data = all_obj_data.replace("https: //",'https://') 
    all_obj_data = all_obj_data.replace('jpg""}','jpg"}')
    
    return "{}".format(all_obj_data) 
    
    

################################################################
################################################################
## Function dependencies, for example:
## package>=version
#pymongo[tls,srv]==3.6.1
#Flask==1.0.2