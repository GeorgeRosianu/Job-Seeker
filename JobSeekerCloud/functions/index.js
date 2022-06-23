const functions = require("firebase-functions");

// The Firebase Admin SDK to access Cloud Firestore.
const admin = require("firebase-admin");
admin.initializeApp();

exports.helloWorld = functions.https.onRequest((request, response) => {
  functions.logger.info("Hello logs!", {structuredData: true});
  response.send("Hello from Firebase!");
});

exports.addUserToFirestore = functions.auth.user().onCreate((user) => {
  const usersRef = admin.firestore().collection("users");
  let username = "";
  let image = "";

  if (user.displayName == null) {
    username = "User";
  } else {
    username = user.displayName;
  }

  if (user.photoURL == null) {
    image = "";
  } else {
    image = user.photoURL;
  }

  return usersRef.doc(user.uid).set({
    userId: user.uid,
    displayName: username,
    userEmail: user.email,
    imageUri: image,
    firstName: "",
    lastName: "",
    phoneNumber: "",
    age: 0,
    residence: "",
    sex: "",
    educationLevel: "",
    educationSpec: "",
    educationCity: "",
    educationInstitution: "",
    educationDate: "",
    experiencePosition: "",
    experienceCompany: "",
    experienceCity: "",
    experienceIndustry: "",
    experienceYears: "",
    experienceDescription: "",
    resumes: null,
    favorites: null,
  });
});
