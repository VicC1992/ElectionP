document.addEventListener("DOMContentLoaded", function () {
    // Obține select-ul și formularele
    const voteRoundSelect = document.getElementById("voteRound");
    const addForm = document.getElementById("addForm");
    const deleteForm = document.getElementById("deleteForm");

    // Adaugă un eveniment de trimitere pe fiecare formular
    addForm.addEventListener("submit", function () {
        document.getElementById("voteRoundIdAdd").value = voteRoundSelect.value;
    });
    deleteForm.addEventListener("submit", function () {
        document.getElementById("voteRoundIdDelete").value = voteRoundSelect.value;
    });
});
