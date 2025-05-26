// Carga y muestra la lista de reservas al iniciar la página
fetchReservas();

// Evento submit para crear una nueva reserva
document.getElementById("form-reserva").addEventListener("submit", async function (e) {
    e.preventDefault(); // Evita que el formulario recargue la página

    // Crea un objeto reserva con los valores del formulario
    const reserva = {
        dni: document.getElementById("dni").value,               // DNI del cliente
        usuario: document.getElementById("usuario").value,       // Nombre de usuario
        vueloId: parseInt(document.getElementById("vueloId").value, 10), // ID del vuelo (número)
        hotelId: parseInt(document.getElementById("hotelId").value, 10)  // ID del hotel (número)
    };

    try {
        // Petición POST para enviar la nueva reserva al backend
        const response = await fetch(`${API}/reservas`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(reserva) // Convierte a JSON para enviar
        });

        if (!response.ok) {
            // Si la respuesta es error, lee el mensaje y lo muestra
            const errorData = await response.json();
            alert("Error: " + (errorData.message || "Error desconocido"));
            return; // Sale para no continuar
        }

        fetchReservas(); // Refresca la lista para incluir la nueva reserva
        this.reset();    // Limpia el formulario

    } catch (error) {
        // Captura errores de red o problemas inesperados
        alert("Error al guardar reserva: " + error.message);
    }
});

// Evento submit para actualizar una reserva existente
document.getElementById("form-actualizar-reserva").addEventListener("submit", async function (e) {
    e.preventDefault();

    const id = document.getElementById("id-actualizar").value; // ID de la reserva a actualizar

    // Obtiene los datos actualizados del formulario y convierte IDs a números
    const vueloId = parseInt(document.getElementById("vueloId-actualizar").value, 10);
    const hotelId = parseInt(document.getElementById("hotelId-actualizar").value, 10);
    const dni = document.getElementById("dni-actualizar").value;
    const usuario = document.getElementById("usuario-actualizar").value;

    // Validación: ambos IDs deben ser números válidos
    if (isNaN(vueloId) || isNaN(hotelId)) {
        alert("Debe seleccionar un vuelo y un hotel válidos para actualizar la reserva.");
        return; // Sale sin hacer nada
    }

    // Objeto con los datos nuevos para enviar en la petición PUT
    const reservaActualizada = {
        dni,
        usuario,
        vueloId,
        hotelId
    };

    try {
        // Petición PUT para actualizar la reserva en la API
        const respuesta = await fetch(`${API}/reservas/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(reservaActualizada)
        });

        if (!respuesta.ok) {
            // Si hay error, muestra el mensaje recibido del servidor
            const errorData = await respuesta.json();
            alert("Error al actualizar reserva: " + (errorData.message || "Los hoteles o vuelos indicados no se encontraron o no están disponibles "));
            return;
        }

        alert("Reserva actualizada correctamente"); // Confirma que se actualizó bien
        fetchReservas(); // Recarga la lista para mostrar cambios
        this.reset();    // Limpia el formulario de actualización

    } catch (error) {
        alert("Error al actualizar reserva: " + error.message); // Error inesperado
    }
});

// Función para obtener y mostrar todas las reservas en la tabla
async function fetchReservas() {
    const res = await fetch(`${API}/reservas`); // Petición GET a la API
    const reservas = await res.json();          // Convierte JSON a objeto JS

    const tabla = document.getElementById("tabla-reservas");
    tabla.innerHTML = ""; // Limpia la tabla antes de llenarla

    reservas.forEach(r => {
        const row = document.createElement("tr");

        // Rellena la fila con los datos de la reserva
        row.innerHTML = `
            <td>${r.id}</td>
            <td>${r.dni}</td>
            <td>${r.usuario}</td>
            <td>${r.vueloAsociado?.id ?? ''}</td> <!-- ID vuelo asociado o vacío -->
            <td>${r.hotelAsociado?.id ?? ''}</td> <!-- ID hotel asociado o vacío -->
            <td></td> <!-- Aquí irán los botones -->
        `;

        // Botón para editar la reserva, rellena el formulario con los datos
        const btnEditar = document.createElement("button");
        btnEditar.textContent = "Editar";
        btnEditar.onclick = () => {
            document.getElementById("id-actualizar").value = r.id ?? "";
            document.getElementById("dni-actualizar").value = r.dni ?? "";
            document.getElementById("usuario-actualizar").value = r.usuario ?? "";
            document.getElementById("vueloId-actualizar").value = r.vueloAsociado?.id ?? "";
            document.getElementById("hotelId-actualizar").value = r.hotelAsociado?.id ?? "";
        };

        // Botón para eliminar la reserva
        const btnEliminar = document.createElement("button");
        btnEliminar.textContent = "Eliminar";
        btnEliminar.onclick = async () => {
            try {
                const res = await fetch(`${API}/reservas/${r.id}`, { method: "DELETE" });
                if (!res.ok) {
                    const errorData = await res.json();
                    alert("No se puede eliminar la reserva: " + errorData.message);
                } else {
                    fetchReservas(); // Refresca la lista tras eliminar
                }
            } catch (error) {
                alert("Error al eliminar la reserva."); // Error inesperado
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
