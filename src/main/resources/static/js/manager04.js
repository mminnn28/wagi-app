document.addEventListener("DOMContentLoaded", function () {
  document.querySelectorAll(".authority").forEach((button) => {
    button.addEventListener("click", function () {
      if (!this.classList.contains("admin") && !this.classList.contains("chairman")) {
        this.classList.add("admin");
        this.classList.remove("chairman");
        this.textContent = "운영진";
      } else if (!this.classList.contains("chairman")) {
        this.classList.add("chairman");
        this.textContent = "회장단";
      } else {
        this.classList.remove("admin", "chairman");
        this.textContent = "일반";
      }
    });
  });
});
