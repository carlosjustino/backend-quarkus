package br.com.justino.projeto7.helper;

import br.com.justino.projeto7.exceptions.ContainerError;
import br.com.justino.projeto7.exceptions.GenericConstraintValidation;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.*;

public final class StaticFunctions {

    public static void throwConstraintViolationException(String mensagem, Object instancia) {
        final Set<ConstraintViolation<?>> erros = new LinkedHashSet();
        erros.add(new GenericConstraintValidation(mensagem));
        String baseMessage = mensagem;
        if (instancia != null)
            baseMessage = instancia.getClass().getSimpleName() + ": " + baseMessage;
        throw new ConstraintViolationException(baseMessage, erros);
    }

    public static List<ContainerError> getValidationErrors(final Set<ConstraintViolation<?>> violations) {
        final List<ContainerError> errors = new LinkedList<>();
        for (final ConstraintViolation<?> violation : violations) {
            errors.add(new ContainerError(violation.getMessage()));
        }
        return errors;
    }

    public static String getMessageConstraintViolationException(Exception exception) {
        if (exception == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(extractDeveloperMessageException(exception));
        if (exception.getCause() instanceof ConstraintViolationException) {
            sb.append(getMessageConstraintViolationException((ConstraintViolationException) exception.getCause()));
        }
        return sb.toString();
    }

    public static String extractDeveloperMessageException(Throwable exception) {
        StringBuilder sb = new StringBuilder();
        if (exception instanceof ConstraintViolationException) {
            ConstraintViolationException exc = (ConstraintViolationException) exception;
            List<ContainerError> errors = getValidationErrors(exc.getConstraintViolations());
            if (errors.isEmpty()) {
                sb.append(StringHelper.cleaner(exc.getMessage()));
                sb.append("\n");
                sb.append(StringHelper.cleaner(exc.getLocalizedMessage()));
                sb.append("\n");
            } else {
                for (ContainerError ve : errors) {
                    sb.append(StringHelper.cleaner(ve.getMessage()));
                    sb.append("\n");
                }
            }
        }else if (exception.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            org.hibernate.exception.ConstraintViolationException cx = (org.hibernate.exception.ConstraintViolationException)exception.getCause();
            if (cx.getSQLException() != null)
                sb.append(cx.getSQLException());
            else
                sb.append(cx.toString());
        } else {
            sb.append(exception.getMessage());
        }
        return sb.toString();
    }

    public static boolean isEmpty(Object object) {
        boolean isEmpty = false;
        if (object == null) {
            isEmpty = true;
        } else if (object instanceof Boolean) {
            isEmpty = ((Boolean) object).equals(Boolean.FALSE);
        } else if (object instanceof String) {
            isEmpty = "".equals(object);
        } else if (object instanceof Integer) {
            isEmpty = ((Integer) object).equals(0);
        } else if (object instanceof Long) {
            isEmpty = ((Long) object).equals(0l);
        } else if (object instanceof Double) {
            isEmpty = round((Double) object, 10).equals(0D);
        } else if (object instanceof List) {
            isEmpty = ((List) object).isEmpty();
        }
        return isEmpty;
    }
    public static Double round(Double valor, int escala) {
        Double retorno = null;
        if (valor != null) {
            BigDecimal bdConv = new BigDecimal(String.valueOf(valor));
            bdConv = bdConv.setScale(escala + 1, BigDecimal.ROUND_HALF_UP);
            bdConv = bdConv.setScale(escala, BigDecimal.ROUND_HALF_UP);
            retorno = bdConv.doubleValue();
        }

        return retorno;
    }
    public static boolean objectEquals(Object a, Object b) {
        if (a == null && b == null) {
            return true;
        } else if (a == null) {
            if (b instanceof List)
                return isEmpty(b);
            else
                return false;
        } else if (b == null) {
            if (a instanceof List)
                return isEmpty(a);
            else
                return false;
        } else if (a instanceof Integer && b instanceof Long) {
            return objectEquals(a, Integer.valueOf(((Long) b).intValue()));
        } else if (a instanceof Long && b instanceof Integer) {
            return objectEquals(a, Long.valueOf(((Integer) b).longValue()));
        } else if (a instanceof PanacheEntity && b instanceof PanacheEntity
                && (a.getClass().isInstance(b) || b.getClass().isInstance(a))) {
            Long ida, idb;
            ida = ((PanacheEntity) a).id;
            idb = ((PanacheEntity) b).id;
            return objectEquals(ida, idb);
        } else if (a instanceof Double && b instanceof Double) {
            return compareTo((Double) a, (Double) b) == 0;
        } else if (a instanceof Date && b instanceof Date) {
            return DateTimeHelper.objectEquals((Date) a, (Date) b);
        } else {
            return a.equals(b);
        }
    }

    public static int compareTo(Double objeto1, Double objeto2) {
        return (objeto1 == null && objeto2 == null ? 0
                : (objeto1 != null && objeto2 != null ? Double.compare(round(objeto1, 10), round(objeto2, 10))
                : (objeto1 != null ? 1 : -1)));
    }

    public static int compareTo(Long objeto1, Integer objeto2) {
        return (objeto1 == null && objeto2 == null ? 0
                : (objeto1 != null && objeto2 != null ? Long.compare(objeto1, objeto2)
                : (objeto1 != null ? 1 : -1)));
    }

    public static int compareTo(Integer objeto1, Long objeto2) {
        return (objeto1 == null && objeto2 == null ? 0
                : (objeto1 != null && objeto2 != null ? Integer.compare(objeto1, objeto2.intValue())
                : (objeto1 != null ? 1 : -1)));
    }

    public static int compareTo(Comparable objeto1, Comparable objeto2) {
        return (objeto1 == null && objeto2 == null ? 0
                : (objeto1 != null && objeto2 != null ? objeto1.compareTo(objeto2)
                : (objeto1 != null ? 1 : -1)));
    }

    public static int compareTo(PanacheEntity objeto1, PanacheEntity objeto2) {
        Integer retorno = null;
        if (objeto1 == null && objeto2 == null)
            retorno = 0;
        else if (objeto1 == null)
            retorno = -1;
        else if (objeto2 == null)
            retorno = 1;
        else
            retorno = compareTo(objeto1.id, objeto2.id);
        return retorno;
    }


    public static Integer toInteger(Double decimal) {
        if (decimal == null)
            return 0;
        return Integer.valueOf(decimal.intValue());
    }

    public static Integer toInteger(Long longo) {
        if (longo == null)
            return 0;
        return new Integer(longo.intValue());
    }

    /**
     * Retorna um valor Inteiro convertendo a String.
     *
     * @param texto String no qual sera transformado para Inteiro
     * @return faz a conversao para inteiro
     */
    public static Integer toInteger(String texto) {
        Integer retorno = null;
        try {
            retorno = Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            throwConstraintViolationException(e.getMessage(), texto);
        }

        return retorno;
    }

    public static Integer getHour(Date pDataHora) {
        return DateTimeHelper.getHour(pDataHora);
    }

    public static Integer getMinute(Date pDataHora) {
        return DateTimeHelper.getMinute(pDataHora);
    }

    public static Integer getSecond(Date pDataHora) {
        return DateTimeHelper.getSecond(pDataHora);
    }


}
