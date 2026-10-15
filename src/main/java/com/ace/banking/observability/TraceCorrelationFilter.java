package com.ace.banking.observability;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapSetter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class TraceCorrelationFilter extends OncePerRequestFilter {
    private static final TextMapGetter<HttpServletRequest> REQUEST_GETTER = new TextMapGetter<>() {
        @Override
        public Iterable<String> keys(HttpServletRequest carrier) {
            return Collections.list(carrier.getHeaderNames());
        }

        @Override
        public String get(HttpServletRequest carrier, String key) {
            return carrier.getHeader(key);
        }
    };

    private static final TextMapSetter<HttpServletResponse> RESPONSE_SETTER =
            (carrier, key, value) -> carrier.setHeader(key, value);

    private final Tracer tracer;
    private final io.opentelemetry.api.OpenTelemetry openTelemetry;

    public TraceCorrelationFilter(Tracer tracer, io.opentelemetry.api.OpenTelemetry openTelemetry) {
        this.tracer = tracer;
        this.openTelemetry = openTelemetry;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        Context parent = openTelemetry.getPropagators()
                .getTextMapPropagator()
                .extract(Context.current(), request, REQUEST_GETTER);
        Span span = tracer.spanBuilder(request.getMethod() + " " + request.getRequestURI())
                .setParent(parent)
                .setSpanKind(SpanKind.SERVER)
                .startSpan();

        try (var scope = span.makeCurrent()) {
            openTelemetry.getPropagators()
                    .getTextMapPropagator()
                    .inject(Context.current(), response, RESPONSE_SETTER);
            span.setAttribute("http.request.method", request.getMethod());
            span.setAttribute("url.path", request.getRequestURI());
            filterChain.doFilter(request, response);
            span.setAttribute("http.response.status_code", response.getStatus());
        } catch (IOException | ServletException | RuntimeException exception) {
            span.recordException(exception);
            throw exception;
        } finally {
            span.end();
        }
    }
}
