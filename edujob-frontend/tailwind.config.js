/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,ts}"],
  theme: {
    extend: {
      colors: {
        "edu-blue": "#1a649a", // El azul de las letras y el engranaje
        "edu-orange": "#f28e20", // El naranja del engranaje
      },
    },
  },
  plugins: [],
};
