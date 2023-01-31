package fr.isen.knackisen.androidprojet.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fr.isen.knackisen.androidprojet.databinding.FragmentRightBinding

class RightFragment : Fragment() {
    private var _binding: FragmentRightBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRightBinding.inflate(inflater, container, false)
        return binding.root
    }
}