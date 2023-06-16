import os
from flask import Flask, request
import tensorflow as tf
import numpy as np
import matplotlib.image as mpimg

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
    model = tf.lite.Interpreter(model_file)
    model.allocate_tensors()

    # Save the uploaded file
    uploaded_file = request.files['img']
    if uploaded_file.filename != '':
        # Save the uploaded file to a temporary location
        uploaded_image_path = 'uploaded_images/' + uploaded_file.filename
        uploaded_file.save(uploaded_image_path)

        # Read the image from the saved file into a numpy array
        img = mpimg.imread(uploaded_image_path)
        # Convert to float32
        img = tf.cast(img, tf.float32) / 255
        # Resize to 224x224 (size the model is expecting)
        img = tf.image.resize(img, [224, 224])
        # Expand img dimensions from (224, 224, 3) to (1, 224, 224, 3)
        img = np.expand_dims(img, axis=0)

        # Set the input tensor for the model
        input_details = model.get_input_details()
        model.set_tensor(input_details[0]['index'], img)

        # Run the inference
        model.invoke()

        # Get the output tensor and predictions
        output_details = model.get_output_details()
        predictions = model.get_tensor(output_details[0]['index'])
        predicted_label = np.argmax(predictions, axis=1)

        percentage = "{:.2f}".format(np.max(predictions) * 100)
        class_names = ['Rendah', 'Sedang', 'Tinggi']
        predicted_class = { "Kadar" : class_names[0]}

        # Remove the temporary uploaded image file
        os.remove(uploaded_image_path)

        return predicted_class, percentage

    return "No file uploaded"

if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True)