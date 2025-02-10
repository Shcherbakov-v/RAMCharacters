package com.mydoctor.ramcharacters.ui.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mydoctor.ramcharacters.data.repository.CharacterRepository
import com.mydoctor.ramcharacters.model.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(characterRepository: CharacterRepository) :
    ViewModel() {
    val characters: Flow<PagingData<Character>> =
        characterRepository.getCharacters().cachedIn(viewModelScope)
}