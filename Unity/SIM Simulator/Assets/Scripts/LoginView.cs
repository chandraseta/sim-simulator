using System.Collections;
using System.Collections.Generic;
using MarkLight;
using MarkLight.Views.UI;
using UnityEngine;


[HideInPresenter]
public class LoginView : UIView {

	public string Email;
	public string Password;
	public InputField EmailField;
	public InputField PasswordField;
	public Button LoginButton;
	public User CurrentUser;
	private int prevId;
	public bool loggedIn {get; private set;}

	public void Start() {
		CurrentUser = User.Instance;
		prevId = 0;
	}

	public void Update() {
		if (CurrentUser.Id != prevId && !loggedIn) {
			prevId = CurrentUser.Id;
			setDisabledState();
			loggedIn = true;
		}
	}


	public void Login() {
		if (!loggedIn) {
			Debug.Log("Login() called | Email: " + Email + ", Password: " + Password);
			CurrentUser.Email = Email;
			CurrentUser.Password = Password;
			StartCoroutine(CurrentUser.Login());
		} else {
			Debug.Log("Logout() called");
			loggedIn = false;
			prevId = 0;
			CurrentUser.ResetUser();
			setEnabledState();
		}
	}

	private void setDisabledState() {
		EmailField.SetState("Disabled");
		EmailField.BackgroundColor.Value = new Color(0.8F, 0.8F, 0.8F, 1F);
		PasswordField.SetState("Disabled");
		PasswordField.BackgroundColor.Value = new Color(0.8F, 0.8F, 0.8F, 1F);
		LoginButton.Text.Value = "Logout";
	}

	private void setEnabledState() {
		EmailField.SetState("Default");
		EmailField.BackgroundColor.Value = Color.white;
		EmailField.Text.Value = "";
		PasswordField.SetState("Default");
		PasswordField.BackgroundColor.Value = Color.white;
		PasswordField.Text.Value = "";
		LoginButton.Text.Value = "Login";
	}
}
