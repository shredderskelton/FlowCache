package com.skelton.flowcache.account.create

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skelton.flowcache.AppConfig
import com.skelton.flowcache.account.AccountDataSourceRest
import com.skelton.flowcache.system.HttpProviderDefault
import com.skelton.flowcache.ui.theme.FlowCacheTheme
import kotlinx.parcelize.Parcelize

class CreateActivity : ComponentActivity() {

    private val viewModel: CreateViewModel by lazy {
        val config = AppConfig()
        DefaultCreateViewModel(
            AccountDataSourceRest(
                HttpProviderDefault
            )
        )
    }

    @Parcelize
    data class UserData(
        val id: String, val name: String, val email: String, val address: String
    ) : Parcelable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FlowCacheTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column() {
                        val userData: MutableState<UserData> = rememberSaveable {
                            mutableStateOf(
                                UserData(
                                    id = "nick",
                                    name = "Nick Skelton",
                                    email = "nick.g.skelton@gmail.com",
                                    address = "76 Frederick Lane",
                                )
                            )
                        }
                        val textModifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth()

                        Text("Create an account")
                        OutlinedTextField(
                            modifier = textModifier,
                            value = userData.value.id,
                            onValueChange = { userData.getAndUpdate { copy(id = it) } },
                            label = { Text("Name") })
                        OutlinedTextField(
                            modifier = textModifier,
                            value = userData.value.name,
                            onValueChange = { userData.getAndUpdate { copy(name = it) } },
                            label = { Text("Name") })
                        OutlinedTextField(
                            modifier = textModifier,
                            value = userData.value.email,
                            onValueChange = { userData.getAndUpdate { copy(email = it) } },
                            label = { Text("Email") })
                        OutlinedTextField(
                            modifier = textModifier,
                            value = userData.value.address,
                            onValueChange = { userData.getAndUpdate { copy(address = it) } },
                            label = { Text("Address") })
                        val buttonMod = Modifier.weight(1F)
                        Button(
                            modifier = buttonMod
                                .fillMaxWidth()
                                .padding(end = 6.dp),
                            onClick = {
                                with(userData.value) {
                                    viewModel.create(
                                        id = id,
                                        name = name,
                                        email = email,
                                        address = address,
                                    )
                                }
                            }) {
                            Text("Create")
                        }
                    }
                }
            }
        }
    }
}

fun <T> MutableState<T>.getAndUpdate(block: T.() -> T) {
    this.value = value.block()
}