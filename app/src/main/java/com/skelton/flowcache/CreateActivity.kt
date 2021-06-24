package com.skelton.flowcache

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.skelton.flowcache.databinding.ActivityCreateBinding

class CreateActivity : AppCompatActivity() {

    private val viewModel: CreateViewModel by lazy {
        val config = AppConfig()
        DefaultCreateViewModel(
            DefaultAccountDataSource(
                DefaultFirestoreCollectionProvider(),
                config
            )
        )
    }

    private lateinit var binding: ActivityCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        with(binding) {
            binding.createButton.setOnClickListener {
                viewModel.create(
                    id = accountEditText.text.toString(),
                    name = nameEditText.text.toString(),
                    email = emailEditText.text.toString(),
                    address = addressEditText.text.toString(),
                )
            }
        }
    }
}
