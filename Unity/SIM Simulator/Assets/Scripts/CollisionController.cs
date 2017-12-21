using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;

public class CollisionController : MonoBehaviour {

    public Collider carFrontLeft;
    public Collider carFrontRight;
    public Collider carRearLeft;
    public Collider carRearRight;

	public Text resultText;

    public float currentTimer;
    public float DEFAULT_TIMER = 10.0f;

    public bool multipleCheckpoint;
    public Transform checkpoint1;
    public Transform checkpoint2;

	private bool pass = false;
	private bool fail = false;

    void Start() {
        currentTimer = DEFAULT_TIMER;
        if (multipleCheckpoint) {
            checkpoint2.gameObject.SetActive(false);
        }
		resultText.text = "";
    }

    void Update() {
		if (pass) {
			resultText.text = "Congratulations! You Pass! Press 'Q' to quit";
			if (Input.GetKeyDown(KeyCode.Q)) {
                string SceneName = SceneManager.GetActiveScene().name;
                int SceneNo = SceneName[SceneName.Length - 1] - '0';

                if (SceneNo > User.Instance.PracticalScore) {
                    User.Instance.PracticalScore = SceneNo;
                }
                StartCoroutine(User.Instance.UpdateScore());

                SceneManager.LoadScene ("MainMenu");
			}
		}
		else if (fail) {
			resultText.text = "You Fail! Press 'R' to restart or 'Q' to quit";
			if (Input.GetKeyDown(KeyCode.R)) {
				SceneManager.LoadScene (SceneManager.GetActiveScene().name);
			} else if (Input.GetKeyDown(KeyCode.Q)) {
				SceneManager.LoadScene ("MainMenu");
			}
		}
    }

    public void OnCollisionEnter(Collision other) {
        if (other.gameObject.CompareTag("CollisionObject")) {
            if (!pass) {
                fail = true;
            }
        }
    }
    public void OnTriggerEnter(Collider other) {
        if (other.gameObject.CompareTag("Goal")) {
            currentTimer = DEFAULT_TIMER;
        }
        if (other.gameObject.CompareTag("CheckpointTrack")) {
            Destroy(other.gameObject);
            if (IsAllCheckpointTrackDone() && IsAllCheckpointDone() && !fail) {
				pass = true;
            }
        }
    }

    public void OnTriggerStay(Collider other) {
        if (other.gameObject.CompareTag("Goal")) {
            if (other.bounds.Intersects(carFrontLeft.bounds) && other.bounds.Intersects(carFrontRight.bounds) && other.bounds.Intersects(carRearLeft.bounds) && other.bounds.Intersects(carRearRight.bounds)) {
                currentTimer -= Time.deltaTime;
                if (currentTimer <= 0) {
                    Destroy(other.gameObject);
                    if (multipleCheckpoint && checkpoint2.gameObject.activeSelf == false) {
                        checkpoint2.gameObject.SetActive(true);
                    }
                    else {
                        if (IsAllCheckpointTrackDone() && IsAllCheckpointDone() && !fail) {
                            pass = true;
                        }
                    }
                }
            }
        }
    }

    public void OnTriggerExit(Collider other) {
        if (other.gameObject.CompareTag("Goal")) {
            currentTimer = DEFAULT_TIMER;
        }
    }

    public bool IsAllCheckpointDone() {
        GameObject[] checkpoint;
        bool done = false;
        checkpoint = GameObject.FindGameObjectsWithTag("Goal");
        done = checkpoint.Length == 0 || checkpoint.Length == 1;
        Debug.Log("CP = " + checkpoint.Length);
        return done;
    }

    public bool IsAllCheckpointTrackDone() {
        GameObject[] checkpointTrack;
        bool done = false;
        checkpointTrack = GameObject.FindGameObjectsWithTag("CheckpointTrack");
        done = checkpointTrack.Length == 0 || checkpointTrack.Length == 1;
        Debug.Log("CPT = " + checkpointTrack.Length);
        return done;
    }
}