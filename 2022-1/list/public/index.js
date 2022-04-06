let ls = document.getElementById("list")
const url = "https://pokeapi.co/api/v2/pokemon/ditto"

document.addEventListener("DOMContentLoaded", loadList)

async function loadList(){
    let content = await (await fetch(url)).json()
    let li = document.createElement("li")
    li.innerHTML = JSON.stringify(content["abilities"][0]["ability"]["name"])
    ls.appendChild(li)
}
  