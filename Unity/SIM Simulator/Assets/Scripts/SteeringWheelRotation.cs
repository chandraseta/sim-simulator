using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace EVP {
    public class SteeringWheelRotation : MonoBehaviour {

        private float rotations;
        private float turnSpeed = 250;
        private float turnSpeedFeedback = 500;
        private float turnSpeedForce = 750;
        private Quaternion DEFAULT_ROTATION;
        // Use this for initialization
        void Start() {
            DEFAULT_ROTATION = transform.localRotation;
            rotations = transform.localRotation.eulerAngles.z;
        }

        // Update is called once per frame
        void Update() {
            if (Input.GetKey(KeyCode.D)) {
                if (rotations <= 0) {
                    transform.Rotate(Vector3.forward * Time.deltaTime * turnSpeed);
                    rotations -= Time.deltaTime * turnSpeed;
                }
                else {
                    transform.Rotate(Vector3.forward * Time.deltaTime * turnSpeedForce);
                    rotations -= Time.deltaTime * turnSpeedForce;
                }
            }
            else if (Input.GetKey(KeyCode.A)) {
                if (rotations > 0) {
                    transform.Rotate(-Vector3.forward * Time.deltaTime * turnSpeed);
                    rotations += Time.deltaTime * turnSpeed;
                }
                else {
                    transform.Rotate(-Vector3.forward * Time.deltaTime * turnSpeedForce);
                    rotations += Time.deltaTime * turnSpeedForce;
                }      
            }
             else    //rotate to default
             {
                if (rotations < -1) {
                    transform.Rotate(-Vector3.forward * Time.deltaTime * turnSpeedFeedback);
                    rotations += Time.deltaTime * turnSpeedFeedback;
                }
                else if (rotations > 1) {
                    transform.Rotate(Vector3.forward * Time.deltaTime * turnSpeedFeedback);
                    rotations -= Time.deltaTime * turnSpeedFeedback;
                }
                else if (rotations >= -1 && rotations <=1) {
                    transform.localRotation = DEFAULT_ROTATION;
                    rotations = 0;
                }
            }
            //Vector3 currentRotation = transform.localRotation.eulerAngles;
            //if (currentRotation.z > 90 || currentRotation.z < -90) {
            //    currentRotation.z = Mathf.Clamp(currentRotation.z, -90, 90);
            //    transform.localRotation = Quaternion.Euler(currentRotation);
            //}
        }
    }
}

