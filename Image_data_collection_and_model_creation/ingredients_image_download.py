# Note:- Code was written in ipynb with drive integration. If you need with as standlone script change the drive location refrence to os.dir().

from google_images_download import google_images_download

from google.colab import drive
drive.mount('/content/drive')

path = 'drive/My Drive/Cook_smart_collect_images/'



ingredients = ['vegetable drumstick','watermelon','papaya','pear','fruit apple','kidney bean','raw chicken','jackfruit',
'guava','ginger','cranberries','garlic','eggplant','raw prawn','raw mussels','raw fish','zucchini','pomegranate',
'pineapple','hazelnut','cayenne pepper','nutmeg','raw masoor dal','cashew','gourd','pomegranate','tomato','raisins','asparagus',
'stalks celery','raw moong dal','potatoes','pumpkin','onion','cauliflower','thyme','turnip','coriander leaf','lemon','chives',
'chickpeas','grape','bell pepper','vanilla','peas','coconut','mushrooms','carrot',
'mint leaf','green chilli','red chilli','okra','tamarind','fenugreek leaves','fenugreek seed','cinnamon','cucumber',
'rice','bay leaf','broccoli','corn','egg','peanut','bananas','orange','dates','kiwis','spinach','paneer','shallot',
'mango','lettuce','almonds','pistachio','apricot','yam','strawberries','rosemary','oregano','radishes']

def download_images(keys, no_of_url_required):
    response = google_images_download.googleimagesdownload()   #class instantiation
    arguments = {"keywords": keys,"limit":no_of_url_required,"print_urls":True}   
    paths = response.download(arguments)  
    # print(paths)   

img_down_path = '/content/drive/My Drive/Cook_smart_collect_images/downloads' 

# Most of the time i found out that google_image_download coudn't download the required number of images in one go.
# In order to avoid this irregularity I have added this piece of code so 
# it will keep on try to download the required #images. 
# i.e if on the first try if it downlaods 250 images from 500 images it will go to 2nd iteration
# to download rest 250 images.
def manage_download(items,no_of_url_required): 
    content_show = ''
    #Download Images for the first time
    print("### Downloading {} images for {} #####".format(no_of_url_required, items))
    download_images(items,no_of_url_required)

    list_in_downloads = os.listdir(img_down_path)
    for i in range(len(list_in_downloads)):
        folder_name = list_in_downloads[i]
        print("working on folder: {}".format(folder_name))
        # Check Length of images downloded
        list_of_images = os.listdir(img_down_path+'/{}'.format(folder_name))
        if len(list_of_images) < no_of_url_required:
            # download_images(folder_name,(no_of_url_required-len(list_of_images)) )
            print("###########################################")
            print("### Downloading {} images for {} #####".format((no_of_url_required-len(list_of_images)), folder_name))
            print("###########################################")
            # Only downlaod for specific folder missing image counts
            # download_images(folder_name,int((no_of_url_required-len(list_of_images))))
            manage_download(folder_name,int((no_of_url_required-len(list_of_images))))
        else:
            break
    else:
        for j in range(len(list_in_downloads)):
            folder = list_in_downloads[j]
            content_show += str(folder) +":: "+ str(len(os.listdir(img_down_path+'/{}'.format(folder)))) + " ::"

    return str(content_show)

  
# Appending to a single line so that it easier to pass in argument in google_image_download
keywordstr = ",".join(x for x in ingredients)
result = manage_download(keywordstr,500)

# Here Your can see all images being downloaded sucessfully.
os.chdir(base_dir)
!ls