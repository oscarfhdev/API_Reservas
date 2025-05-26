// Carga y muestra la lista de vuelos cuando la página carga
fetchVuelos();

// Escucha el evento submit del formulario para añadir un nuevo vuelo
document.getElementById("form-vuelo").addEventListener("submit", async function (e) {
    e.preventDefault(); // Evita que el formulario recargue la página

    // Crea un objeto vuelo con los datos del formulario
    const vuelo = {
        compania: document.getElementById("compania").value,
        fecha: document.getElementById("fecha").value, // Fecha en formato YYYY-MM-DD
        precio: parseFloat(document.getElementById("precio").value), // Convierte a número decimal
        plazasDisponibles: parseInt(document.getElementById("plazas").value, 10) // Convierte a entero
    };

    // Envía la petición POST para crear el vuelo en el backend
    await fetch(`${API}/vuelos`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(vuelo) // Envía los datos en formato JSON
    });

    fetchVuelos(); // Actualiza la lista con el nuevo vuelo
    this.reset();  // Limpia el formulario para otro vuelo nuevo
});

// Función para obtener y mostrar todos los vuelos en la tabla
async function fetchVuelos() {
    const res = await fetch(`${API}/vuelos`); // Pide la lista de vuelos
    const vuelos = await res.json();           // Convierte la respuesta JSON en objeto JS
    const tabla = document.getElementById("tabla-vuelos");
    tabla.innerHTML = "";                      // Limpia la tabla para rellenarla

    // Por cada vuelo, crea una fila en la tabla con sus datos
    vuelos.forEach(v => {
        const row = document.createElement("tr");

        // Introduce las celdas con la información del vuelo
        row.innerHTML = `
            <td>${v.id}</td>
            <td>${v.compania}</td>
            <td>${formatearFecha(v.fecha)}</td> <!-- Formatea la fecha para mejor lectura -->
            <td>${v.precio}</td>
            <td>${v.plazasDisponibles}</td>
            <td></td> <!-- Aquí van los botones -->
        `;

        // Botón para editar el vuelo, rellena el formulario de actualización con los datos del vuelo
        const btnEditar = document.createElement("button");
        btnEditar.textContent = "Editar";
        btnEditar.onclick = () => {
            document.getElementById("id-actualizar").value = v.id;
            document.getElementById("compania-actualizar").value = v.compania;
            document.getElementById("fecha-actualizar").value = v.fecha;
            document.getElementById("precio-actualizar").value = v.precio;
            document.getElementById("plazas-actualizar").value = v.plazasDisponibles;
        };

        // Botón para eliminar el vuelo, envía petición DELETE al backend
        const btnEliminar = document.createElement("button");
        btnEliminar.textContent = "Eliminar";
        btnEliminar.onclick = async () => {
            try {
                const res = await fetch(`${API}/vuelos/${v.id}`, { method: "DELETE" });
                if (!res.ok) {
                    // Si no se puede eliminar, muestra mensaje de error
                    const errorData = await res.json();
                    alert("No se puede eliminar el vuelo: " + errorData.message);
                } else {
                    fetchVuelos(); // Actualiza la lista después de eliminar
                }
            } catch (error) {
                alert("Error al eliminar el vuelo."); // Error en la petición
            }
        };

        // Añade los botones a la última celda de la fila
        const tdAcciones = row.querySelector("td:last-child");
        tdAcciones.appendChild(btnEditar);
        tdAcciones.appendChild(btnEliminar);

        // Añade la fila a la tabla
        tabla.appendChild(row);
    });
}

// Función para formatear la fecha ISO a DD/MM/YYYY
function formatearFecha(fechaISO) {
    const fecha = new Date(fechaISO);
    const dia = String(fecha.getDate()).padStart(2, '0');
    const mes = String(fecha.getMonth() + 1).padStart(2, '0'); // Los meses empiezan en 0
    const año = fecha.getFullYear();
    return `${dia}/${mes}/${año}`;
}

// Escucha el evento submit del formulario para actualizar un vuelo existente
document.getElementById("form-actualizar-vuelo").addEventListener("submit", async function (e) {
    e.preventDefault(); // Evita recargar la página

    const id = document.getElementById("id-actualizar").value; // ID del vuelo a actualizar

    // Objeto con los datos nuevos introducidos en el formulario
    const vueloActualizado = {
        compania: document.getElementById("compania-actualizar").value,
        fecha: document.getElementById("fecha-actualizar").value,
        precio: parseFloat(document.getElementById("precio-actualizar").value),
        plazasDisponibles: parseInt(document.getElementById("plazas-actualizar").value, 10)
    };

    // Envía petición PUT para actualizar el vuelo en la API
    const respuesta = await fetch(`${API}/vuelos/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(vueloActualizado)
    });

    if (respuesta.ok) {
        alert("Vuelo actualizado correctamente");
        fetchVuelos(); // Recarga la lista con los datos actualizados
        this.reset();  // Limpia el formulario de actualización
    } else {
        alert("Error al actualizar el vuelo");
    }
});
