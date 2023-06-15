from flask import Flask, request
from tensorflow import keras
import tensorflow as tf
import numpy as np
import matplotlib.image as mpimg
import tensorflow_hub as hub

app = Flask(__name__)

@app.route("/")
def showHomePage():
    return "This is the home page"

@app.route('/predicts', methods=['POST','GET'])
def get_category():
    """Function to Predict the Class Name
    Args:
        None
    Returns:
        [str]: Prediction
        [str]: Percentage
    """
    model_file = 'Klasifikasi_Glukosa.tflite'
    model = keras.models.load_model(model_file, custom_objects={
                                    'KerasLayer': hub.KerasLayer})

    # Save the uploaded file
    uploaded_file = request.files['file']
    if uploaded_file.filename != '':
        file_path = 'uploaded_images/' + uploaded_file.filename
        uploaded_file.save(file_path)

        # Read the image from the saved file into a numpy array
        img = mpimg.imread(file_path)
        # Convert to float32
        img = tf.cast(img, tf.float32) / 255
        # Resize to 224x224 (size the model is expecting)
        img = tf.image.resize(img, [224, 224])
        # Expand img dimensions from (224, 224, 3) to (1, 224, 224, 3) for set_tensor method call
        img = np.expand_dims(img, axis=0)
        images = np.vstack([img])

        prediction = model(images)
        predicted_label = np.argmax(prediction, axis=1)

        percentage = "{:.2f}".format(np.max(prediction) * 100)
        class_names = ['Rendah', 'Sedang', 'Tinggi']
        predicted_class = class_names[predicted_label[0]]

        # Remove the saved file
        os.remove(file_path)

        return predicted_class, percentage

    return "No file uploaded"

if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True)

