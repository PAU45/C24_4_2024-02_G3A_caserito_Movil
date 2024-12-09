package com.melendez.paulo.frontend_proyecto.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.melendez.paulo.frontend_proyecto.LoginActivity
import com.melendez.paulo.frontend_proyecto.R
import com.melendez.paulo.frontend_proyecto.fragments.empresa.EmpresaHomeFragment
import com.melendez.paulo.frontend_proyecto.network.ApiClient
import com.melendez.paulo.frontend_proyecto.network.ApiService
import com.melendez.paulo.frontend_proyecto.network.UserResponse
import com.melendez.paulo.frontend_proyecto.network.RoleRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private lateinit var tvAlert: TextView
    private lateinit var tvUsername: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvDireccion: TextView
    private lateinit var tvTelefono: TextView
    private lateinit var btnUpdateProfile: Button
    private lateinit var btnChangeToEmpresa: Button
    private lateinit var btnLogout: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicialización de vistas
        tvAlert = view.findViewById(R.id.tvAlert)
        tvUsername = view.findViewById(R.id.tvUsername)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvDireccion = view.findViewById(R.id.tvDireccion)
        tvTelefono = view.findViewById(R.id.tvTelefono)
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile)
        btnChangeToEmpresa = view.findViewById(R.id.btnChangeToEmpresa)
        btnLogout = view.findViewById(R.id.btnLogout)

        val token = getTokenFromPreferences()
        val roles = getRolesFromPreferences()

        // Verificar token y cargar perfil
        if (token != null) {
            fetchUserProfile(token)
        } else {
            showAlert("No se encontró token.")
        }

        // Cargar datos del usuario desde SharedPreferences
        loadUserFromPreferences()

        // Configurar listeners de botones
        btnUpdateProfile.setOnClickListener {
            navigateToUpdateProfile()
        }

        btnLogout.setOnClickListener { logout() }

        // Mostrar u ocultar el botón basado en los roles del usuario
        Log.d("ProfileFragment", "User roles: $roles")
        if (roles?.roles?.contains("EMPRESA") == true) {
            btnChangeToEmpresa.visibility = View.VISIBLE
            btnChangeToEmpresa.setOnClickListener {
                navigateToEmpresaFragments()
            }
        } else {
            btnChangeToEmpresa.visibility = View.GONE
        }
    }

    private fun loadUserFromPreferences() {
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        tvUsername.text = sharedPreferences.getString("usuario", "Usuario desconocido")
        tvEmail.text = sharedPreferences.getString("email", "Email no disponible")
        tvDireccion.text = sharedPreferences.getString("direccion", "Dirección no disponible")
        tvTelefono.text = sharedPreferences.getString("telefono", "Teléfono no disponible")
    }

    private fun getTokenFromPreferences(): String? {
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", null)
    }

    private fun getRolesFromPreferences(): RoleRequest? {
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val rolesString = sharedPreferences.getString("roles", null)
        return rolesString?.let { RoleRequest(it) }
    }

    private fun fetchUserProfile(token: String) {
        val apiService = ApiClient.getClient(requireContext()).create(ApiService::class.java)
        val call = apiService.getUserProfile("Bearer $token")

        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    tvUsername.text = userResponse?.usuario ?: "Usuario desconocido"
                    tvEmail.text = userResponse?.email ?: "Email no disponible"
                    tvDireccion.text = userResponse?.direccion ?: "Dirección no disponible"
                    tvTelefono.text = userResponse?.telefono ?: "Teléfono no disponible"

                    // Guardar datos en SharedPreferences
                    saveUserToPreferences(userResponse)
                } else {
                    showAlert("Error al recuperar los datos del perfil.")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                showAlert("Fallo en la conexión.")
            }
        })
    }

    private fun saveUserToPreferences(userResponse: UserResponse?) {
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("usuario", userResponse?.usuario)
            putString("email", userResponse?.email)
            putString("direccion", userResponse?.direccion)
            putString("telefono", userResponse?.telefono)
            putString("roles", userResponse?.roles?.joinToString(",")) // Guarda los roles como una cadena separada por comas
            apply()
        }
    }



    private fun navigateToUpdateProfile() {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, UpdateProfileFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    private fun navigateToEmpresaFragments() {
        // Navegar a los fragments de empresa
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, EmpresaHomeFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun logout() {
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }

        // Redirigir al LoginActivity después de cerrar sesión
        startActivity(Intent(requireActivity(), LoginActivity::class.java))
        requireActivity().finish()
    }

    private fun showAlert(message: String) {
        tvAlert.text = message
        tvAlert.visibility = View.VISIBLE
    }
}