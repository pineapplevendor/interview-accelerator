function showElement(id) {
  document.getElementById(id).style.display = 'block';
}

const questionDivPrefix = 'new-question-';
const addQuestionButtonPrefix = 'add-question-';
[...Array(100).keys()].forEach(function(idx) {
  const currentId = addQuestionButtonPrefix + String(idx);
  const nextButton = document.getElementById(currentId);
  nextButton.addEventListener('click', function() {
    const nextId = questionDivPrefix + String(idx + 1);
    showElement(nextId);
  });
});

