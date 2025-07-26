const inputElements = document.querySelectorAll(".property-input");
const containerElements = document.querySelectorAll(
  ".property-input-container"
);
const selectLibraryElement = document.querySelector("#libraryId");
const selectStudentElement = document.querySelector("#studentId");
const clearLibraryButton = document.querySelector(".clear-library-selection");
const clearStudentButton = document.querySelector(".clear-student-selection");
const submitButton = document.querySelector(".submit-btn");
const maxDate = document.getElementById("publishDate");
const canSelectStudent = false;

selectLibraryElement.selectedIndex = actualLibraryId;
selectStudentElement.selectedIndex = actualStudentId;

if (actualLibraryId === 0) {
  selectStudentElement.disabled = true;
}

maxDate.setAttribute("max", getFormattedDate());

inputElements.forEach((inputElement, i) => {
  inputElement.addEventListener("focus", () => {
    containerElements[i].style.width = "350px";
  });

  inputElement.addEventListener("blur", () => {
    containerElements[i].style.width = "";
  });
});

clearLibraryButton.addEventListener("click", () => {
  selectLibraryElement.selectedIndex = 0;
  selectStudentElement.selectedIndex = 0;
  selectStudentElement.disabled = true;
});

clearStudentButton.addEventListener("click", () => {
  selectStudentElement.selectedIndex = 0;
});

selectLibraryElement.addEventListener("change", function (event) {
  const selectedOption = event.target.value;
  const firstOptionValue = selectLibraryElement.options[0].value;
  if (selectedOption === firstOptionValue) {
    selectStudentElement.disabled = true;
    selectStudentElement.selectedIndex = 0;
  } else {
    selectStudentElement.disabled = false;
  }
});

function getFormattedDate() {
  const today = new Date();
  const year = today.getFullYear();
  const month = (today.getMonth() + 1).toString().padStart(2, "0");
  const day = today.getDate().toString().padStart(2, "0");
  return year + "-" + month + "-" + day;
}

function validateAuthor () {
  if (!isNaN(author)) {
    alert("Author field cannot contain only numbers");
    return false;
  }
  return true;
}

function validateForm() {
  const author = document.getElementById("author").value;
  if (
    selectStudentElement.selectedIndex === 0 ||
    selectLibraryElement.selectedIndex === 0
  ) {
    alert("Please select a Library and a Student");
    return false;
  } else if (!isNaN(author)) {
    alert("Author field cannot contain only numbers");
    return false;
  }
  return true;
}
