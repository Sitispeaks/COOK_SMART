# Importing all necessary imports
from flask import abort
import pymongo
import json
import re
from flask import Flask
from flask import jsonify 
    
    
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
    query_ingredients = []
    
    # squares = [x**2 for x in range(10)] 
    # ingredients name to filter list maping
    
    if request_json and 'ingredients' in request_json:
        if len(str(request_json['ingredients'])) != 0:
            for each_ingredient in request_json['ingredients'].split(","):
                each_query = {}
                each_query['recipe_ingredient.ingr'] = re.compile(r"{}".format(each_ingredient))
                query_ingredients.append(each_query)
    
    # Mongo Query Setup
    # mongodb+srv://<username>:<password>@cooksmart-nsxth.mongodb.net/test?retryWrites=true&w=majority
    myclient = pymongo.MongoClient("mongodb+srv://tapan:root@cooksmart-nsxth.mongodb.net/test?retryWrites=true&w=majority")
    mydb = myclient["cook_smart"]
    mycol = mydb["all_indian_recipe"]
    
    # Sample query
    # { $and:[ {"recipe_ingredient.ingr": /chicken/}, {"recipe_ingredient.ingr": /butter/} ] }
    
    filter_ing={'$and': query_ingredients }
    result = mycol.find(filter=filter_ing)
    
    result_receipes_arr = []

    for each_response_ele in result:
        each_response_ele['_id'] = str(each_response_ele['_id'])
        result_receipes_arr.append(each_response_ele)
    
    #result_receipes_str = '['+ ','.join(str(e) for e in result_receipes_arr)+']'
    #result_receipes_str = result_receipes_str.replace("'",'"')
    #result_receipes_str = result_receipes_str.replace(' " "','"')
    #result_receipes_str = result_receipes_str.replace('""','"')
    #result_receipes_str = result_receipes_str.replace(' "','"')
    #result_receipes_str = result_receipes_str.replace('https: //','https://')
    
    all_recive_data = "{}".format(result_receipes_arr)
    all_recive_data = all_recive_data.replace("'",'"')
    all_recive_data = all_recive_data.replace('"url": " "}]','"url": ""}]')
    all_recive_data = all_recive_data.replace('" "','"')
    all_recive_data = all_recive_data.replace("https: //",'https://') 
    all_recive_data = all_recive_data.replace('jpg""}','jpg"}')
    
    return "{}".format(all_recive_data) 
    
    
################################################################
################################################################
## Function dependencies, for example:
## package>=version
#pymongo[tls,srv]==3.6.1
#Flask==1.0.2
