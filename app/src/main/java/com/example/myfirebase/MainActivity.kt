package com.example.myfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myfirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("ProductDb")

        if(auth.currentUser != null){
            binding.tvResult.text = auth.currentUser!!.email
        }

        binding.btnRegister.setOnClickListener(){
            registerNewUser ("someone@gmail.com", "123456")
        }

        binding.btnLogin.setOnClickListener(){
            loginUser("someone@gmail.com", "123456")
        }

        binding.btnLogout.setOnClickListener(){
            logoutCurrentUser()
        }

        binding.btnInsert.setOnClickListener() {
            var newProduct = Product("P001", "Pencil", 3.50)
            addNewProduct(newProduct)

            newProduct = Product("P002", "Pencil", 3.50)
            addNewProduct(newProduct)

            newProduct = Product("P003", "Ruler", 2.50)
            addNewProduct(newProduct)

            newProduct = Product("P004", "Paper", 1.50)
            addNewProduct(newProduct)
        }

        binding.btnDelete.setOnClickListener() {
            val key = "P004"
            deleteProduct(key)
        }

        binding.btnGet.setOnClickListener() {
            val key = "P003"
            searchProduct(key)
        }
    }

    private fun searchProduct(key: String) {
        database.child("productTable")
            .child(key).get()
            .addOnSuccessListener { result->
                binding.tvResult.text = result.child("description").value.toString()
            }
            .addOnFailureListener { ex->
                binding.tvResult.text = ex.message
            }
    }

    private fun deleteProduct(key: String) {
        database.child("productTable")
            .child(key).removeValue()
            .addOnSuccessListener {
                binding.tvResult.text = "Delete"
            }
            .addOnFailureListener { ex->
                binding.tvResult.text = ex.message
            }
    }

    private fun addNewProduct(newProduct: Product) {
        database.child("productTable")
            .child(newProduct.id).setValue(newProduct)
            .addOnSuccessListener {
                binding.tvResult.text = "New product added"
            }
            .addOnFailureListener { ex->
                binding.tvResult.text = ex.message
            }
    }

    private fun logoutCurrentUser() {
        auth.signOut()
        binding.tvResult.text = "signed out"
    }

    private fun loginUser(email: String, psw: String) {
        auth.signInWithEmailAndPassword(email, psw)
            .addOnSuccessListener {
                binding.tvResult.text = email
            }
            .addOnFailureListener { ex->
                binding.tvResult.text = ex.message
            }
    }

    private fun registerNewUser(email: String, psw: String) {
        auth.createUserWithEmailAndPassword(email, psw)
            .addOnSuccessListener {
                binding.tvResult.text = "New User added"
            }
            .addOnFailureListener { ex->
                binding.tvResult.text = ex.message
            }
    }
}