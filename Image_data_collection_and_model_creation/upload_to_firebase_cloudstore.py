# pip install google-cloud-storage
# pip install firebase


# from google.cloud import storage
# from firebase import firebase
# import os


# os.environ["GOOGLE_APPLICATION_CREDENTIALS"]="<add your credentials path>"
# firebase = firebase.FirebaseApplication('<your firebase database path>')
# client = storage.Client()
# bucket = client.get_bucket('<your firebase storage path>')
# # posting to firebase storage
# imageBlob = bucket.blob("/")
# # imagePath = [os.path.join(self.path,f) for f in os.listdir(self.path)]
# imagePath = "<local_path>/image.png"
# imageBlob = bucket.blob("<image_name>")
# imageBlob.upload_from_filename(imagePath)


import firebase_admin
from firebase_admin import credentials, firestore, storage

cred=credentials.Certificate('C:\\Users\\blackturtle\\Envs\\tube\\ps.json')
firebase_admin.initialize_app(cred, {
    'storageBucket': 'gs://dene-2ac17.appspot.com'
})
db = firestore.client()
bucket = storage.bucket()
blob = bucket.blob('hello.txt')
outfile='C:\\Users\\blackturtle\\Envs\\tube\\hello.txt'
with open(outfile, 'rb') as my_file:
    blob.upload_from_file(my_file)
