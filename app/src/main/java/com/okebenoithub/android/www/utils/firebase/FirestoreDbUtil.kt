package com.okebenoithub.android.www.utils.firebase

import android.content.Context
import com.okebenoithub.android.www.R
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

/**
 * Firebase FireStore :: contain every recurring task dealing with FireStore database
 */
class FirestoreDbUtil @Inject constructor() {

    /**
     * Access a Cloud Firestore instance
     * @return void
     */
    private fun getFirestoreInstance(): FirebaseFirestore {
        return Firebase.firestore
    }

    // interface that will check for specific doc data get process status
    interface GetDocDataWithSpecificIdProcessCallback {
        fun onGetDocDataProcessStatus(
            isSuccessful: Boolean,
            errorMessage: String?,
            docRefId: String?
        )

        fun onGetDocumentData(documentSnapshot: DocumentSnapshot)
    }

    /**
     * Get doc data from collection with specific doc ID
     * @param collectionName :: collection name
     * @param docSpecificId :: specific doc ID
     * @param getDocDataWithSpecificIdProcessCallback
     */
    fun getDocDataFromCollectionWithSpecificId(
        context: Context,
        collectionName: String,
        docSpecificId: String,
        getDocDataWithSpecificIdProcessCallback: GetDocDataWithSpecificIdProcessCallback
    ) {
        val docRef = getFirestoreInstance().collection(collectionName).document(docSpecificId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    getDocDataWithSpecificIdProcessCallback.onGetDocDataProcessStatus(
                        true,
                        null,
                        document.id
                    )
                    getDocDataWithSpecificIdProcessCallback.onGetDocumentData(document)
                } else {
                    getDocDataWithSpecificIdProcessCallback.onGetDocDataProcessStatus(
                        false, context.getString(
                            R.string.no_data_found
                        ), null
                    )
                }
            }
            .addOnFailureListener { exception ->
                getDocDataWithSpecificIdProcessCallback.onGetDocDataProcessStatus(
                    false,
                    exception.localizedMessage,
                    null
                )
            }
    }

    // interface that will check for all docs data get process status
    interface GetAllDocsDataFromCollectionProcessCallback {
        fun onGetAllDocsDataProcessStatus(isSuccessful: Boolean, errorMessage: String?)
        fun onGetDocumentsData(documents: List<QueryDocumentSnapshot>)
    }

    /**
     * Get all docs from a Firestore collection
     * @param collectionName :: collection name
     * @param basedOnFieldKey :: based on field key
     * @param basedOnFieldValue :: based on field value
     * @param orderByFieldKey :: order by field key
     * @param orderByFieldValue :: order by field value
     * @param dataSource :: data source
     * @param getAllDocsDataFromCollectionProcessCallback
     */
    fun getAllDocsDataFromCollection(
        context: Context,
        collectionName: String,
        basedOnFieldKey: String?,
        basedOnFieldValue: Any?,
        orderByFieldKey: String?,
        orderByFieldValue: Query.Direction = Query.Direction.DESCENDING,
        dataSource: Source,
        getAllDocsDataFromCollectionProcessCallback: GetAllDocsDataFromCollectionProcessCallback
    ) {
        if (basedOnFieldKey == null && basedOnFieldValue == null) {
            // retrieve all documents in a collection by omitting the where()
            getFirestoreInstance().collection(collectionName)
                .orderBy(orderByFieldKey.toString(), orderByFieldValue)
                .get(dataSource)
                .addOnSuccessListener { result ->
                    val docs = ArrayList<QueryDocumentSnapshot>()
                    for (document in result) {
                        docs.add(document)
                        //Log.d(TAG, "${document.id} => ${document.data}")
                    }
                    if (docs.isNotEmpty()) {
                        getAllDocsDataFromCollectionProcessCallback.onGetAllDocsDataProcessStatus(
                            true,
                            null
                        )
                        getAllDocsDataFromCollectionProcessCallback.onGetDocumentsData(docs)
                    } else {
                        getAllDocsDataFromCollectionProcessCallback.onGetAllDocsDataProcessStatus(
                            false,
                            context.getString(R.string.no_data_found)
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    getAllDocsDataFromCollectionProcessCallback.onGetAllDocsDataProcessStatus(
                        false,
                        exception.localizedMessage
                    )
                }
        } else {
            getFirestoreInstance().collection(collectionName)
                .orderBy(orderByFieldKey.toString(), orderByFieldValue)
                .whereEqualTo(basedOnFieldKey.toString(), basedOnFieldValue.toString()).get(dataSource)
                .addOnSuccessListener { result ->
                    val docs = ArrayList<QueryDocumentSnapshot>()
                    for (document in result) {
                        docs.add(document)
                        //Log.d(TAG, "${document.id} => ${document.data}")
                    }
                    if (docs.isNotEmpty()) {
                        getAllDocsDataFromCollectionProcessCallback.onGetAllDocsDataProcessStatus(
                            true,
                            null
                        )
                        getAllDocsDataFromCollectionProcessCallback.onGetDocumentsData(docs)
                    } else {
                        getAllDocsDataFromCollectionProcessCallback.onGetAllDocsDataProcessStatus(
                            false,
                            context.getString(R.string.no_data_found)
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    getAllDocsDataFromCollectionProcessCallback.onGetAllDocsDataProcessStatus(
                        false,
                        exception.localizedMessage
                    )
                }
        }
    }

    // interface that will check for generated doc data add process status
    interface AddDocDataWithGeneratedIdProcessCallback {
        fun onAddedDocDataProcessStatus(
            isSuccessful: Boolean,
            errorMessage: String?,
            docRefId: String?
        )
    }

    /**
     * Add doc data to collection with
     * generated doc id
     * @param collectionName :: collection name
     * @param docData :: doc data
     * @param addDocDataWithGeneratedIdProcessCallback
     * Eg:
     * val docData = hashMapOf(
         "first" to "Ada",
         "last" to "Lovelace",
         "born" to 1815
         )
     */
    fun addDocDataToCollectionWithGeneratedId(
        collectionName: String,
        docData: Any,
        addDocDataWithGeneratedIdProcessCallback: AddDocDataWithGeneratedIdProcessCallback
    ) {
        // Add a new document with a generated ID
        getFirestoreInstance().collection(collectionName)
            .add(docData)
            .addOnSuccessListener { documentReference ->
                addDocDataWithGeneratedIdProcessCallback.onAddedDocDataProcessStatus(
                    true,
                    null,
                    documentReference.id
                )
            }
            .addOnFailureListener { e ->
                // error adding document
                addDocDataWithGeneratedIdProcessCallback.onAddedDocDataProcessStatus(
                    false,
                    e.localizedMessage,
                    null
                )
            }
    }

    // interface that will check for generated doc data add process status
    interface AddDocDataWithSpecificIdProcessCallback {
        fun onAddedDocDataProcessStatus(isSuccessful: Boolean, errorMessage: String?)
    }

    /**
     * Add doc data to collection with specific doc ID
     * @param collectionName :: collection name
     * @param docSpecificId :: specific doc ID
     * @param docData :: doc data
     * @param addDocDataWithSpecificIdProcessCallback
     */
    fun addDocDataToCollectionWithSpecificId(
        collectionName: String,
        docSpecificId: String,
        docData: Any,
        addDocDataWithSpecificIdProcessCallback: AddDocDataWithSpecificIdProcessCallback
    ) {
        // add a new document with specific ID
        getFirestoreInstance().collection(collectionName).document(docSpecificId)
            .set(docData, SetOptions.merge())
            .addOnSuccessListener {
                addDocDataWithSpecificIdProcessCallback.onAddedDocDataProcessStatus(true, null)
            }
            .addOnFailureListener { e ->
                addDocDataWithSpecificIdProcessCallback.onAddedDocDataProcessStatus(
                    false,
                    e.localizedMessage
                )
            }
    }

    // interface that will check for updated doc field data process status
    interface UpdateDocFieldDataProcessCallback {
        fun onUpdatedDocFieldDataProcessStatus(isSuccessful: Boolean, errorMessage: String?)
    }

    /**
     * Update document data field
     * @param collectionName :: collection name
     * @param docSpecificId :: document ID
     * @param fieldDataKey :: field data key
     * @param fieldDataNewValue :: field new data value
     * @param updateDocFieldDataProcessCallback
     */
    fun updateDocDataField(
        collectionName: String,
        docSpecificId: String,
        fieldDataKey: String,
        fieldDataNewValue: Any,
        updateDocFieldDataProcessCallback: UpdateDocFieldDataProcessCallback
    ) {
        getFirestoreInstance().collection(collectionName).document(docSpecificId)
            .update(fieldDataKey, fieldDataNewValue)
            .addOnSuccessListener {
                updateDocFieldDataProcessCallback.onUpdatedDocFieldDataProcessStatus(true, null)
            }
            .addOnFailureListener { e ->
                updateDocFieldDataProcessCallback.onUpdatedDocFieldDataProcessStatus(
                    false,
                    e.localizedMessage
                )
            }
    }

    /**
     * Update document data field in Collection
     * @param collectionName :: collection name
     * @param docSpecificId :: document ID
     * @param updateDocDataFields => type mapOf("age" to 13,"favorites.color" to "Red")
     */
    fun updateDocDataFields(
        collectionName: String,
        docSpecificId: String,
        updateDocDataFields: Map<String, Any>,
        updateDocFieldDataProcessCallback: UpdateDocFieldDataProcessCallback
    ) {
        getFirestoreInstance().collection(collectionName).document(docSpecificId)
            .update(updateDocDataFields)
            .addOnSuccessListener {
                updateDocFieldDataProcessCallback.onUpdatedDocFieldDataProcessStatus(true, null)
            }
            .addOnFailureListener { e ->
                updateDocFieldDataProcessCallback.onUpdatedDocFieldDataProcessStatus(
                    false,
                    e.localizedMessage
                )
            }
    }

    /**
     * Update doc data array object field
     * @param collectionName :: collection name
     * @param docSpecificId :: document ID
     * @param fieldArrayKey :: field array key
     * @param fieldArrayOldValue
     * @param fieldArrayNewValue
     * @param updateDocFieldDataProcessCallback
     */
    fun updateDocDataArrayObjectField(
        collectionName: String,
        docSpecificId: String,
        fieldArrayKey: String,
        fieldArrayOldValue: Any,
        fieldArrayNewValue: Any,
        updateDocFieldDataProcessCallback: UpdateDocFieldDataProcessCallback
    ) {
        getFirestoreInstance().collection(collectionName).document(docSpecificId)
            .update(fieldArrayKey, FieldValue.arrayRemove(fieldArrayOldValue))
            .addOnCompleteListener { removeTask ->
                if (removeTask.isSuccessful) {
                    val docRefId =
                        getFirestoreInstance().collection(collectionName).document(docSpecificId)
                    docRefId.update(fieldArrayKey, FieldValue.arrayUnion(fieldArrayNewValue))
                        .addOnCompleteListener { additionTask ->
                            if (additionTask.isSuccessful) {
                                updateDocFieldDataProcessCallback.onUpdatedDocFieldDataProcessStatus(
                                    true,
                                    null
                                )
                            } else {
                                additionTask.exception?.message?.let {
                                    updateDocFieldDataProcessCallback.onUpdatedDocFieldDataProcessStatus(
                                        false,
                                        it
                                    )
                                }
                            }
                        }
                } else {
                    removeTask.exception?.message?.let {
                        updateDocFieldDataProcessCallback.onUpdatedDocFieldDataProcessStatus(
                            false,
                            it
                        )
                    }
                }
            }
    }

    // interface that will check for delete doc data process status
    interface DeleteDocDataFromCollectionProcessCallback {
        fun onDeleteDocDataProcessStatus(isSuccessful: Boolean, errorMessage: String?)
    }

    /**
     * Delete doc data from collection
     * @param collectionName :: collection name
     * @param docSpecificId :: specific doc ID
     * @param deleteDocDataFromCollectionProcessCallback
     */
    fun deleteDocDataFromCollection(
        collectionName: String,
        docSpecificId: String,
        deleteDocDataFromCollectionProcessCallback: DeleteDocDataFromCollectionProcessCallback
    ) {
        getFirestoreInstance().collection(collectionName).document(docSpecificId)
            .delete()
            .addOnSuccessListener {
                deleteDocDataFromCollectionProcessCallback.onDeleteDocDataProcessStatus(
                    true,
                    null
                )
            }
            .addOnFailureListener { e ->
                deleteDocDataFromCollectionProcessCallback.onDeleteDocDataProcessStatus(
                    false,
                    e.localizedMessage
                )
            }
    }
}