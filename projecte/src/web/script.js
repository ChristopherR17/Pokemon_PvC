// Función para navegar entre las secciones con animaciones
function navigateTo(sectionId) {
  // Obtenemos todas las secciones
  const sections = document.querySelectorAll('main > section');
  sections.forEach(section => {
    section.classList.remove('active');
    section.classList.remove('fade-in');
  });

  // Mostramos la sección seleccionada
  const targetSection = document.getElementById(sectionId);
  targetSection.classList.add('active');

  // Aplicamos la animación de entrada estilo "fade-in"
  setTimeout(() => {
    targetSection.classList.add('fade-in');
  }, 50);

  // Agregamos una animación especial de "destello" en el botón activado
  const buttons = document.querySelectorAll('nav a');
  buttons.forEach(button => button.classList.remove('pokemon-glow'));

  const activeButton = document.querySelector(`nav a[href='#'][onclick*='${sectionId}']`);
  if (activeButton) {
    activeButton.classList.add('pokemon-glow');
  }
}

// Función para animar botones al hacer clic
function animateButton(button) {
  button.classList.add('pokemon-click');
  setTimeout(() => {
    button.classList.remove('pokemon-click');
  }, 300); // La animación dura 300ms
}

// Añadimos el evento de animación a todos los botones
document.querySelectorAll('nav a').forEach(button => {
  button.addEventListener('click', function () {
    animateButton(button);
  });
});
