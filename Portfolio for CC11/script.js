// Basic JavaScript for smooth scrolling when clicking nav links
document.addEventListener('DOMContentLoaded', () => {
  const links = document.querySelectorAll('nav ul li a');

  for (const link of links) {
    link.addEventListener('click', smoothScroll);
  }

  function smoothScroll(event) {
    event.preventDefault();
    const targetId = this.getAttribute('href').substring(1);
    const targetSection = document.getElementById(targetId);
    if (targetSection) {
      window.scrollTo({
        top: targetSection.offsetTop - 20,
        behavior: 'smooth'
      });
    }
  }
});
