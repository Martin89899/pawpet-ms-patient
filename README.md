# ms-patient - Microservicio de Gestión de Mascotas

Microservicio para la gestión de mascotas/pacientes del sistema PawPet. Maneja el CRUD de pacientes, búsqueda avanzada y gestión de archivos multimedia.

## 🚀 Características

- ✅ CRUD completo de pacientes
- ✅ Búsqueda y filtrado avanzado
- ✅ Subida de fotos de mascotas
- ✅ Validación robusta de datos
- ✅ Autenticación mediante ms-auth
- ✅ Logs estructurados con Winston
- ✅ Seguridad con Helmet
- ✅ Tests unitarios con Jest
- ✅ Contenerización con Docker

## 📋 Endpoints

### Pacientes
```
GET    /api/patients              - Listar pacientes (paginado)
GET    /api/patients/:id          - Obtener paciente específico
POST   /api/patients              - Crear nuevo paciente
PUT    /api/patients/:id          - Actualizar paciente
DELETE /api/patients/:id          - Eliminar paciente
POST   /api/patients/:id/photo    - Subir foto de paciente
```

### Búsqueda
```
GET    /api/patients/owner/:ownerId     - Pacientes por propietario
GET    /api/patients/search/query      - Búsqueda avanzada
```

### Sistema
```
GET /health                     - Health check del servicio
```

## 🔧 Instalación

### Prerrequisitos
- Node.js 18+
- PostgreSQL (opcional, usa mock database por defecto)
- Servicio ms-auth funcionando

### Desarrollo Local
```bash
# Clonar repositorio
git clone <repository-url>
cd ms-patient

# Instalar dependencias
npm install

# Configurar variables de entorno
cp .env.example .env
# Editar .env con tus configuraciones

# Iniciar en modo desarrollo
npm run dev
```

### Docker
```bash
# Construir imagen
docker build -t ms-patient .

# Ejecutar contenedor
docker run -p 3002:3002 --env-file .env ms-patient
```

## ⚙️ Configuración

### Variables de Entorno
```bash
# Servidor
PORT=3002
NODE_ENV=development

# Servicio de Autenticación
AUTH_SERVICE_URL=http://localhost:3001

# Logging
LOG_LEVEL=info

# Subida de Archivos
MAX_FILE_SIZE=5242880
UPLOAD_PATH=./uploads

# Base de Datos (opcional)
DB_HOST=localhost
DB_PORT=5432
DB_NAME=pawpet_patients
DB_USER=your_db_user
DB_PASSWORD=your_db_password
```

## 🧪 Testing

```bash
# Ejecutar todos los tests
npm test

# Ejecutar tests con coverage
npm run test:coverage

# Ejecutar tests en modo watch
npm run test:watch
```

## 🏗️ Arquitectura

```
src/
├── routes/          # Definición de rutas
├── controllers/     # Lógica de controladores
├── services/        # Lógica de negocio
├── models/          # Modelos de datos
├── middleware/      # Middleware personalizado
├── utils/           # Utilidades (logger, etc.)
└── app.js           # Aplicación principal
```

## 🔒 Seguridad

- **Helmet**: Protección HTTP básica
- **Rate Limiting**: Límite de peticiones por IP
- **Joi**: Validación robusta de entrada
- **Autenticación**: Integración con ms-auth
- **Validación de archivos**: Control de tamaño y tipo

## 📊 Logs

El servicio utiliza Winston para logging estructurado:

- **Niveles**: error, warn, info, debug
- **Salida**: Archivos y consola
- **Formato**: JSON con metadatos

## 🐳 Docker

### Health Check
```bash
curl http://localhost:3002/health
```

### Logs en Contenedor
```bash
docker logs <container-id>
```

## 🔗 Integración

Este servicio está diseñado para integrarse con:

- **ms-auth**: Validación de usuarios
- **BFF**: Como proxy central
- **ms-historial**: Referencias a pacientes

## 📝 API Documentation

### Crear Paciente
```bash
POST /api/patients
Content-Type: application/json
Authorization: Bearer <token>

{
  "name": "Firulais",
  "species": "dog",
  "breed": "Labrador",
  "birthDate": "2020-01-15",
  "gender": "male",
  "color": "Golden",
  "weight": 25.5,
  "microchip": "1234567890123456",
  "notes": "Mascota muy juguetona"
}
```

### Subir Foto
```bash
POST /api/patients/1/photo
Content-Type: multipart/form-data
Authorization: Bearer <token>

photo: <file>
```

### Buscar Pacientes
```bash
GET /api/patients/search/query?query=labrador&species=dog&age=3
Authorization: Bearer <token>
```

### Response Exitoso
```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "Firulais",
    "species": "dog",
    "breed": "Labrador",
    "birthDate": "2020-01-15",
    "gender": "male",
    "color": "Golden",
    "weight": 25.5,
    "microchip": "1234567890123456",
    "ownerId": 1,
    "photoUrl": "/uploads/patients/patient_1_1234567890.jpg",
    "notes": "Mascota muy juguetona",
    "createdAt": "2023-01-01T00:00:00.000Z",
    "updatedAt": "2023-01-01T00:00:00.000Z"
  }
}
```

## 🚨 Errores Comunes

### 400 - Validation Error
```json
{
  "success": false,
  "message": "Validation error",
  "errors": [
    {
      "field": "name",
      "message": "Name is required"
    }
  ]
}
```

### 401 - Unauthorized
```json
{
  "success": false,
  "message": "Access token is required"
}
```

### 404 - Not Found
```json
{
  "success": false,
  "message": "Patient not found"
}
```

## 🔄 Flujo de Trabajo

1. **Autenticación**: Validar token con ms-auth
2. **Validación**: Verificar datos de entrada con Joi
3. **Procesamiento**: Ejecutar lógica de negocio
4. **Respuesta**: Retornar resultado estructurado
5. **Logging**: Registrar operación

## 📈 Monitoreo

- Health checks automáticos
- Rate limiting por IP
- Logs estructurados
- Métricas de rendimiento
- Validación de autenticación

## 🛠️ Desarrollo

### Agregar Nuevo Endpoint
1. Definir ruta en `routes/patientRoutes.js`
2. Implementar lógica en `controllers/patientController.js`
3. Agregar validación en `middleware/validation.js`
4. Agregar tests en `tests/`

### Manejo de Archivos
```javascript
// Configuración de multer
const multer = require('multer');

const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, 'uploads/patients/');
  },
  filename: (req, file, cb) => {
    const filename = `patient_${req.params.id}_${Date.now()}.jpg`;
    cb(null, filename);
  }
});
```

### Deploy
```bash
# Build para producción
npm run build

# Start en producción
npm start
```

## 📄 Licencia

MIT License

## 👥 Maintainers

PawPet Development Team
