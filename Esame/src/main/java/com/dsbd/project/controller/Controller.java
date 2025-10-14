    package com.dsbd.project.controller;

    import com.dsbd.project.entity.Trip;
    import com.dsbd.project.entity.TripRepository;
    import com.dsbd.project.entity.User;
    import com.dsbd.project.entity.UserRepository;
    import com.dsbd.project.security.AuthResponse;
    import com.dsbd.project.security.JwtUtils;
    import com.dsbd.project.service.ProjectUserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.format.annotation.DateTimeFormat;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.Authentication;
    import org.springframework.web.bind.annotation.*;

    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.stream.Collectors;

    @org.springframework.stereotype.Controller
    @RequestMapping(path = "/user")
    public class Controller {

        @Autowired
        ProjectUserService userService;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private JwtUtils jwtUtils;

        @Autowired
        private TripRepository tripRepository;

        //POST http://localhost:8082/user/register + JSON
        @PostMapping(path = "/register")
        public @ResponseBody User register(@RequestBody User user) { return userService.addUser(user); }

        //POST http://localhost:8082/user/login + JSON
        @PostMapping(path = "/login")
        public @ResponseBody AuthResponse login(@RequestBody User user) { return userService.login(user); }

        //POST http://localhost:8082/user/re-auth + RefreshToken
        @PostMapping(path = "/re-auth")
        public @ResponseBody AuthResponse reAuth(@RequestHeader("Refresh-Token") String refreshToken) throws Exception {
            if (refreshToken == null || refreshToken.isEmpty()) throw new RuntimeException("Refresh token mancante");
            return userService.reAuth(refreshToken);
        }

        //GET http://localhost:8082/user/me + JWT
        @GetMapping("/me")
        public @ResponseBody Map<String, Object> me(Authentication authentication) {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email);

            if (user == null) throw new RuntimeException("Utente non trovato");

            Map<String, Object> response = new HashMap<>();
            response.put("credit", user.getCredit());
            response.put("email", user.getEmail());

            return response;
        }

        //PATCH http://localhost:8082/user/me/credit + JWT + JSON(credit)
        @PatchMapping("/me/credit")
        public ResponseEntity<?> topUpCredit(@RequestBody Map<String, Object> payload, Authentication authentication) {
                String email = authentication.getName();
                double amount = Double.parseDouble(payload.get("credit").toString());

                User user = userRepository.findByEmail(email);
                if (user == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato");
                }

                double currentCredit = user.getCredit();
                user.setCredit(currentCredit + amount);
                userRepository.save(user);

                return ResponseEntity.ok(Map.of(
                        "newCredit", user.getCredit(),
                        "email", user.getEmail()
                ));
            }

        //POST http://localhost:8082/user/trip/create + JWT(admin) + JSON (tutte le informazioni)
        @PostMapping("/trip/create")
        public ResponseEntity<?> createTrip(@RequestHeader("Authorization") String authHeader, @RequestBody Trip trip) {
            try {
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token mancante");
                }

                String token = authHeader.substring(7);

                if (!jwtUtils.validateJwtToken(token)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token non valido");
                }

                String email = jwtUtils.getUserNameFromJwtToken(token);

                User user = userRepository.findByEmail(email);
                if (user == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utente non trovato");
                }

                Trip savedTrip = tripRepository.save(trip);

                return ResponseEntity.ok(savedTrip);

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Errore durante la creazione della corsa: " + e.getMessage());
            }
        }

        //GET http://localhost:8082/user/trips
        /*@GetMapping("/trips")
        public ResponseEntity<List<Trip>> getAllTrips() {
            Iterable<Trip> tripsIterable = tripRepository.findAll();
            List<Trip> trips = new ArrayList<>();
            tripsIterable.forEach(trips::add);
            return ResponseEntity.ok(trips);
        }*/
        @GetMapping("/trips")
        public ResponseEntity<List<Trip>> getAllTrips(
                @RequestParam(required = false) String origin,
                @RequestParam(required = false) String destination,
                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

            // Prendo tutte le corse
            List<Trip> trips = (List<Trip>) tripRepository.findAll();

            // Applico i filtri opzionali
            if (origin != null && !origin.isEmpty()) {
                trips = trips.stream()
                        .filter(trip -> trip.getOrigin().equalsIgnoreCase(origin))
                        .toList();
            }

            if (destination != null && !destination.isEmpty()) {
                trips = trips.stream()
                        .filter(trip -> trip.getDestination().equalsIgnoreCase(destination))
                        .toList();
            }

            if (date != null) {
                LocalDateTime startOfDay = date.atStartOfDay();
                LocalDateTime endOfDay = date.atTime(23, 59, 59);
                trips = trips.stream()
                        .filter(trip -> !trip.getDepartureTime().isBefore(startOfDay)
                                && !trip.getDepartureTime().isAfter(endOfDay))
                        .toList();
            }

            return ResponseEntity.ok(trips);
        }


        //GET http://localhost:8082/user/trip/{tripid}/buy + JWT
        @PostMapping("/trip/{tripId}/buy")
        public ResponseEntity<?> buyTrip(@RequestHeader("Authorization") String authHeader, @PathVariable Integer tripId) {
            try {
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token mancante");
                }

                String token = authHeader.substring(7);

                if (!jwtUtils.validateJwtToken(token)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token non valido");
                }

                String email = jwtUtils.getUserNameFromJwtToken(token);

                User user = userRepository.findByEmail(email);
                if (user == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utente non trovato");
                }

                Trip trip = tripRepository.findById(tripId).orElse(null);
                if (trip == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trip non trovato");
                }

                double userCredit = user.getCredit();
                double tripPrice = trip.getPrice().doubleValue();

                if (userCredit < tripPrice) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Credito insufficiente per acquistare la corsa");
                }

                user.setCredit(userCredit - tripPrice);
                userRepository.save(user);

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Acquisto effettuato con successo");
                response.put("tripId", trip.getId());
                response.put("newCredit", user.getCredit());
                response.put("userEmail", user.getEmail());

                return ResponseEntity.ok(response);

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Errore durante l'acquisto: " + e.getMessage());
            }
        }


    }
