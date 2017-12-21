using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using MarkLight.Views.UI;
using UnityEngine.SceneManagement;

public class MainMenu : UIView {

	public ViewSwitcher ContentViewSwitcher;
	public LoginView loginView;
	public LevelSelectionView levelSelectionView;
	public OptionsView optionsView;

	public void Start() {
		if (User.Instance.Id > 0) {
			ContentViewSwitcher.SwitchTo(1);
		}
	}

	public void Login() {
		Debug.Log("Login() called");
		ContentViewSwitcher.SwitchTo(0);
	}
	
	public void StartGame() {
		Debug.Log("StartGame() called");
		ContentViewSwitcher.SwitchTo(1);
	}

	public void Options() {
		Debug.Log("Options() called");
		ContentViewSwitcher.SwitchTo(2);
	}

	public void Quit() {
		Debug.Log("Quit() called");

		#if UNITY_EDITOR
			UnityEditor.EditorApplication.isPlaying = false;
		#else
			Application.Quit();
		#endif
	}
}
