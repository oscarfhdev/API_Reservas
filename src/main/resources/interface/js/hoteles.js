// Llama a la función que carga y muestra los hoteles al iniciar la página
fetchHoteles();

// Escucha el evento submit del formulario para crear un hotel nuevo
document.getElementById("form-hotel").addEventListener("submit", async function (e) {
    e.preventDefault(); // Evita que el formulario recargue la página

    // Crea un objeto hotel con los datos introducidos en el formulario
    const hotel = {
        nombre: document.getElementById("nombre").value,
        categoria: document.getElementById("categoria").value,
        precio: parseFloat(document.getElementById("precio").value),
        disponibilidad: true  // Nuevo hotel siempre disponible al crear
    };

    // Envía una petición POST a la API para guardar el hotel
    await fetch(`${API}/hoteles`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(hotel) // Convierte el objeto a JSON
    });

    fetchHoteles(); // Recarga la lista de hoteles actualizada
    this.reset();   // Limpia el formulario para nuevas entradas
});

// Función para obtener la lista de hoteles y mostrarlos en la tabla
async function fetchHoteles() {
    const res = await fetch(`${API}/hoteles`); // Pide todos los hoteles al backend
    const hoteles = await res.json(); // Convierte la respuesta JSON a objeto JS
    const tabla = document.getElementById("tabla-hoteles"); // Tabla donde se muestran los hoteles
    tabla.innerHTML = ""; // Vacía la tabla para llenarla de nuevo

    // Por cada hotel, crea una fila con sus datos
    hoteles.forEach(h => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${h.id}</td>
            <td>${h.nombre}</td>
            <td>${h.categoria}</td>
            <td>${h.precio}</td>
            <td>${h.disponibilidad ? "Sí" : "No"}</td>
            <td></td>
        `;

        // Botón para editar el hotel, rellena el formulario de actualización con los datos actuales
        const btnEditar = document.createElement("button");
        btnEditar.textContent = "Editar";
        btnEditar.onclick = () => {
            document.getElementById("id-actualizar").value = h.id;
            document.getElementById("nombre-actualizar").value = h.nombre;
            document.getElementById("categoria-actualizar").value = h.categoria;
            document.getElementById("precio-actualizar").value = h.precio;
            document.getElementById("disponibilidad-actualizar").value = h.disponibilidad;
        };

        // Botón para eliminar el hotel, envía petición DELETE al backend
        const btnEliminar = document.createElement("button");
        btnEliminar.textContent = "Eliminar";
        btnEliminar.onclick = async () => {
            try {
                const res = await fetch(`${API}/hoteles/${h.id}`, { method: "DELETE" });
                if (!res.ok) {
                    // Si hay error, muestra el mensaje recibido desde el backend
                    const errorData = await res.json();
                    alert("No se puede eliminar el hotel: " + errorData.message);
                } else {
                    fetchHoteles(); // Recarga la tabla si se elimina con éxito
                }
            } catch (error) {
                alert("Error al eliminar el hotel."); // Error en la petición
            }
        };

        // Añade los botones al último <td> de la fila
        const tdAcciones = row.querySelector("td:last-child");
        tdAcciones.appendChild(btnEditar);
        tdAcciones.appendChild(btnEliminar);

        // Añade la fila a la tabla
        tabla.appendChild(row);
    });
}

// Escucha el evento submit del formulario para actualizar un hotel
document.getElementById("form-actualizar-hotel").addEventListener("submit", async function (e) {
    e.preventDefault(); // Evita recargar la página

    const id = document.getElementById("id-actualizar").value; // ID del hotel a actualizar

    // Crea un objeto con los datos actualizados del formulario
    const hotelActualizado = {
        nombre: document.getElementById("nombre-actualizar").value,
        categoria: document.getElementById("categoria-actualizar").value,
        precio: parseFloat(document.getElementById("precio-actualizar").value),
        disponibilidad: document.getElementById("disponibilidad-actualizar").value === "true"
    };

    // Envía una petición PUT para actualizar el hotel en el backend
    const respuesta = await fetch(`${API}/hoteles/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(hotelActualizado)
    });

    if (respuesta.ok) {
        alert("Hotel actualizado correctamente");
        fetchHoteles(); // Refresca la tabla con los datos nuevos
        this.reset();   // Limpia el formulario de actualización
    } else {
        alert("Error al actualizar el hotel");
    }
});
