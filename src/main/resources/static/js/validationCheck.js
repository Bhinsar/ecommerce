  // confirm password check
  const password =document.getElementById("password");
  const confirmpassword =document.getElementById("confirmPassword");
  const confirmFeedback = document.getElementById("message")

  confirmpassword.addEventListener("keyup",() =>{
    // Check if both passwords match
    if (password.value !== confirmPassword.value) {
      confirmFeedback.textContent = "Passwords do not match";
      confirmFeedback.style.color = "red";
    }
  })