package com.example.proyecto.data.firebase

import com.example.proyecto.data.entity.Carrito
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class FirebaseCarrito {
    private val db = Firebase.firestore
    private val collectionReference = db.collection("clientes")


    fun getAllCarritos(callback: (ArrayList<Carrito>) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val email = currentUser?.email

        collectionReference
            .document(email.toString())
            .collection("carrito")
            .get()
            .addOnSuccessListener { documents ->
                val carritos = ArrayList<Carrito>()
                for (document in documents) {
                    carritos.add(
                        Carrito(
                            codeCarrito=document.id.split("/").last().toInt(),
                            codeInstrument =document.getDouble("codeInstrument")!!.toInt(),
                            cantidad = document.getDouble("cantidad")!!.toInt(),
                            total = document.getDouble("total")!!
                        )
                    )
                }
                callback(carritos)
            }
    }

    fun create(carrito: Carrito) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val email = currentUser?.email

        val entity = hashMapOf(
            "codeInstrument" to carrito.codeInstrument,
            "cantidad" to carrito.cantidad,
            "total" to carrito.total
        )
        //collectionReference.document(carrito.codeCarrito.toString()).set(entity)

        collectionReference
            .document(email.toString())
            .collection("carrito")
            .document(getRandomCode().toString()).set(entity)
    }

    fun delete(code: Int,callback: (Boolean) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val email = currentUser?.email

        collectionReference
            .document(email.toString())
            .collection("carrito")
            .document(code.toString())
            .delete().addOnSuccessListener {
                callback(true)
        }
    }



    fun getRandomCode():Int {
        var identificador = Date().time.toInt()
        if(identificador<0){
            identificador*=-1
        }
        return identificador
    }


}