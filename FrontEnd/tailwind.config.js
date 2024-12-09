/** @type {import('tailwindcss').Config} */

import defaultTheme from "tailwindcss/defaultTheme";


export default {
  content: ["./index.html",
            "./src/**/*.{html,vue,js,ts}",
            "./node_modules/tw-elements/js/**/*.js"],
  darkMode:'class',
  theme: {
    //擴展功能
    extend: {
      fontFamily:{
        sans:["Open Sans", ...defaultTheme.fontFamily.sans],
      },
      colors:{ //object
        "brand-gray-1":"#dadce0", //自創屬性名稱，代表甚麼顏色，後面要放16進位，之後可以直接調用
        "brand-blue-1":"#1967d2",//在javascript中屬性名稱不能有破折號，所以用雙引號包起來
        "brand-green-1":"#137333",

      }
    },
  },
  // eslint-disable-next-line no-undef
  plugins: [ require ('daisyui'),require("tw-elements/plugin.cjs") ],
  daisyui: { themes: ['bumblebee'],},
}
//text-black

