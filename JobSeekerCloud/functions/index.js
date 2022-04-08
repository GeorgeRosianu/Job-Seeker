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
  return usersRef.doc(user.uid).set({
    userId: user.uid,
    displayName: user.displayName,
    userEmail: user.email,
  });
});
