const inputElements = document.querySelectorAll(".property-input");
const containerElements = document.querySelectorAll(
  ".property-input-container"
);
const selectBooksButton = document.querySelector(".select-many-button");
const booksContainer = document.querySelector(".many-container");

inputElements.forEach((inputElement, i) => {
  inputElement.addEventListener("focus", () => {
    containerElements[i].style.width = "350px";
  });

  inputElement.addEventListener("blur", () => {
    containerElements[i].style.width = "";
  });
});

selectBooksButton.addEventListener("click", () => {
  if (booksContainer.style.opacity != "1") {
    booksContainer.style.visibility = "visible";
    booksContainer.style.opacity = "1";
    booksContainer.style.height = "100%";
  } else {
    booksContainer.style.visibility = "hidden";
    booksContainer.style.opacity = "0";
    booksContainer.style.height = "0";
  }
});

function validateEmail() {
  const email = document.getElementById("email").value;
  const atIndex = email.indexOf("@");
  const dotIndex = email.lastIndexOf(".");

  if (atIndex < 1 || dotIndex < atIndex + 2 || dotIndex + 2 >= email.length) {
    alert("Please enter a valid email address. E.g example@test.ok");
    return false;
  }

  return true;
}
