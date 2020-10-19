package br.com.justino.projeto7.helper;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.text.Normalizer.Form;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StringHelper {
    protected static final String[] MESES_ANO = { "janeiro", "fevereiro", "março", "abril", "maio", "junho", "julho", "agosto", "setembro",
            "outubro", "novembro", "dezembro" };

    public static String format(boolean valor, String formato) {
        if (valor) {
            return formato.split("/")[0];
        } else {
            return formato.split("/")[1];
        }
    }

    public static String repeatString(String str, Integer repeticao) {
        final StringBuilder sb = new StringBuilder(str.length() * repeticao);
        for (int i = 0; i < repeticao; i++) {
            sb.append(str);
        }
        return sb.toString();
    }


    public static String getValuesConcatenated(List<String> lista) {
        StringBuilder retorno = new StringBuilder();
        if (lista != null) {
            lista.forEach(item -> retorno.append(item + lineFeed()));
        }
        return retorno.toString();
    }

    public static String replaceAll(String value, String regex, String replacement) {
        if (value != null)
            return value.replaceAll(regex, replacement);
        else
            return null;
    }

    public static String normalizar(String value) {
        if (value == null)
            return "";
        return Normalizer.normalize(value, Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public static String format(Double value, String format) {
        DecimalFormat df = new DecimalFormat(format, new DecimalFormatSymbols(new Locale("en", "")));
        return df.format(value);
    }

    public static String format(Double value, String format, boolean noLocale) {
        DecimalFormat df = new DecimalFormat(format, new DecimalFormatSymbols(new Locale("pt", "")));
        return df.format(value);
    }


    public static String formatDate(Date dataHora, String formato) {
        if (dataHora == null)
            return "";
        return new SimpleDateFormat(formato).format(dataHora);
    }

    public static String getYearMonth(Date dataHora) {
        return formatDate(dataHora, "yyMM");
    }

    public static String getYearAA(Date dataHora) {
        return formatDate(dataHora, "yy");
    }

    public static String formatDate(Date data) {
        return formatDate(data, "dd/MM/yyyy");
    }

    public static String getValorExtensoMonetario(Double vlr) {

        if (vlr == null)
            return "";

        if (vlr == 0)
            return ("zero");

        long inteiro = (long) Math.abs(vlr); // parte inteira do valor
        double resto = vlr - inteiro; // parte fracionária do valor

        String vlrS = String.valueOf(inteiro);
        if (vlrS.length() > 15)
            return ("Erro: valor superior a 999 trilhões.");

        String s = "", saux, vlrP;
        String centavos = String.valueOf((int) Math.round(resto * 100));

        String[] unidade = { "", "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove", "dez", "onze", "doze", "treze",
                "quatorze", "quinze", "dezesseis", "dezessete", "dezoito", "dezenove" };
        String[] centena = { "", "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos", "setecentos", "oitocentos",
                "novecentos" };
        String[] dezena = { "", "", "vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa" };
        String[] qualificaS = { "", "mil", "milhão", "bilhão", "trilhão" };
        String[] qualificaP = { "", "mil", "milhões", "bilhões", "trilhões" };

        // definindo o extenso da parte inteira do valor
        int n, unid, dez, cent, tam, i = 0;
        boolean umReal = false, tem = false;
        while (!vlrS.equals("0")) {
            tam = vlrS.length();
            // retira do valor a 1a. parte, 2a. parte, por exemplo, para
            // 123456789:
            // 1a. parte = 789 (centena)
            // 2a. parte = 456 (mil)
            // 3a. parte = 123 (milhões)
            if (tam > 3) {
                vlrP = vlrS.substring(tam - 3, tam);
                vlrS = vlrS.substring(0, tam - 3);
            } else { // última parte do valor
                vlrP = vlrS;
                vlrS = "0";
            }
            if (!vlrP.equals("000")) {
                saux = "";
                if (vlrP.equals("100"))
                    saux = "cem";
                else {
                    n = Integer.parseInt(vlrP, 10); // para n = 371, tem-se:
                    cent = n / 100; // cent = 3 (centena trezentos)
                    dez = (n % 100) / 10; // dez = 7 (dezena setenta)
                    unid = (n % 100) % 10; // unid = 1 (unidade um)
                    if (cent != 0)
                        saux = centena[cent];
                    if ((n % 100) <= 19) {
                        if (saux.length() != 0)
                            saux = saux + " e " + unidade[n % 100];
                        else
                            saux = unidade[n % 100];
                    } else {
                        if (saux.length() != 0)
                            saux = saux + " e " + dezena[dez];
                        else
                            saux = dezena[dez];
                        if (unid != 0) {
                            if (saux.length() != 0)
                                saux = saux + " e " + unidade[unid];
                            else
                                saux = unidade[unid];
                        }
                    }
                }
                if (vlrP.equals("1") || vlrP.equals("001")) {
                    if (i == 0) // 1a. parte do valor (um real)
                        umReal = true;
                    else
                        saux = saux + " " + qualificaS[i];
                } else if (i != 0)
                    saux = saux + " " + qualificaP[i];
                if (s.length() != 0)
                    s = saux + ", " + s;
                else
                    s = saux;
            }
            if (((i == 0) || (i == 1)) && s.length() != 0)
                tem = true; // tem centena ou mil no valor
            i = i + 1; // próximo qualificador: 1- mil, 2- milhão, 3- bilhão,
            // ...
        }

        if (s.length() != 0) {
            if (umReal)
                s = s + " real";
            else if (tem)
                s = s + " reais";
            else
                s = s + " de reais";
        }

        // definindo o extenso dos centavos do valor
        if (!centavos.equals("0")) { // valor com centavos
            if (s.length() != 0) // se não é valor somente com centavos
                s = s + " e ";
            if (centavos.equals("1"))
                s = s + "um centavo";
            else {
                n = Integer.parseInt(centavos, 10);
                if (n <= 19)
                    s = s + unidade[n];
                else { // para n = 37, tem-se:
                    unid = n % 10; // unid = 37 % 10 = 7 (unidade sete)
                    dez = n / 10; // dez = 37 / 10 = 3 (dezena trinta)
                    s = s + dezena[dez];
                    if (unid != 0)
                        s = s + " e " + unidade[unid];
                }
                s = s + " centavos";
            }
        }
        return (s);

    }


    public static String toString(Integer vlr) {
        if (vlr == null)
            return "";
        return String.valueOf(vlr);
    }


    public static String toString(Double vlr) {
        if (vlr == null)
            return "";
        String txt = String.valueOf(vlr);
        if (txt.toLowerCase().contains("e")) {
            BigDecimal bigvlr = new BigDecimal(String.valueOf(vlr));
            int escala = 10;
            bigvlr = bigvlr.setScale(escala + 1, BigDecimal.ROUND_HALF_UP);
            bigvlr = bigvlr.setScale(escala, BigDecimal.ROUND_HALF_UP);
            txt = bigvlr.toPlainString() + "#";
            while (txt.endsWith("0#")) {
                txt = txt.replace("0#", "#");
            }
            txt = txt.replace("#", "");
        }
        return txt;
    }

    public static String toString(Date vlr) {
        String retorno = "", formato = "dd/MM/yyyy HH:mm:ssXXX";
        if (vlr != null) {
            if ((StaticFunctions.getHour(vlr) + StaticFunctions.getMinute(vlr) + StaticFunctions.getSecond(vlr)) == 0) {
                formato = "dd/MM/yyyy";
            }
            retorno = formatDate(vlr, formato);
        }

        return retorno;

    }

    public static String toString(String formato, Date vlr) {
        if (vlr == null)
            return "";
        String retorno = null;
        if (formato.equals("DATAHORA_EDOC")) {
            retorno = formatDate(vlr, "yyyy-MM-dd'T'HH:mm:ssXXX");
        } else {
            retorno = formatDate(vlr, formato);
        }
        return retorno;
    }

    public static String toString(Object objeto) {
        String retorno = "";
        if (objeto != null) {
            retorno = objeto.toString();
        }

        return retorno;
    }

    private static final String CARRIAGE_RETURN = "\n";

    public static String lineFeed() {
        return CARRIAGE_RETURN;
    }

    public static String convertFromScientificNotation(double number) {
        // Check if in scientific notation
        if (String.valueOf(number).toLowerCase().contains("e")) {
            System.out.println("The scientific notation number'" + number
                    + "' detected, it will be converted to normal representation with 25 maximum fraction digits.");
            NumberFormat formatter = new DecimalFormat();
            formatter.setMaximumFractionDigits(25);
            return formatter.format(number);
        } else
            return String.valueOf(number);
    }

    public static String concat(String texto, String texto2) {
        return texto.concat(texto2);
    }

    public static String trim(String texto) {
        return texto.trim();
    }

    public static int compareTo(String objeto1, String objeto2) {
        return (objeto1 != null ? objeto1.compareTo(objeto2) : -1);
    }

    public static String replace(String texto, String antigo, String novo) {
        return texto.replace(antigo, novo);
    }

    public static String substring(String texto, Integer inicio) {
        if (texto == null)
            return null;
        return texto.substring(inicio);
    }

    public static String substring(String texto, Integer inicio, Integer fim) {
        if (texto == null)
            return null;
        if (texto.length() >= fim)
            return texto.substring(inicio, fim);
        else
            return texto.substring(inicio, texto.length());
    }

    public static String toUpperCase(String pTexto) {
        return pTexto == null ? pTexto : pTexto.toUpperCase();
    }

    public static String toUpperCaseTrim(String pTexto) {
        return pTexto == null ? pTexto : pTexto.trim().toUpperCase();
    }

    public static String toLowerCase(String pTexto) {
        return pTexto == null ? pTexto : pTexto.toLowerCase();
    }

    public static String toFirstLowerCase(String pTexto) {
        return pTexto == null ? pTexto : pTexto.substring(0, 1).toLowerCase() + pTexto.substring(1);
    }

    public static String toLowerCaseTrim(String pTexto) {
        return pTexto == null ? pTexto : pTexto.trim().toLowerCase();
    }

    public static String stringFormat(Integer qtdZeros, Integer variavel) {
        Integer qtdDigitos = variavel.toString().length();
        if (qtdZeros >= qtdDigitos) {
            qtdZeros = qtdZeros - qtdDigitos;
        } else {
            qtdZeros = 0;
        }
        Integer sumDigitosAndZeros = qtdDigitos + qtdZeros;
        String formato = "%0" + sumDigitosAndZeros + "d";
        return String.format(formato, variavel);
    }

    public static String stringFormat(Integer qtdZeros, String variavel) {
        Integer qtdDigitos = variavel.toString().length();
        if (qtdZeros >= qtdDigitos) {
            qtdZeros = qtdZeros - qtdDigitos;
        } else {
            qtdZeros = 0;
        }
        Integer sumDigitosAndZeros = qtdDigitos + qtdZeros;
        String formato = "%0" + sumDigitosAndZeros + "d";
        return String.format(formato, Integer.valueOf(variavel));
    }

    public static String cleaner(String data) {
        if (data == null)
            return data;
        return data.replace("\"", "'").replace("{", "'").replace("}", "'").replace("[", "'").replace("]", "'");
    }

    public static String makeTextLog(Object objeto) {
        String texto = "#!#";
        if (objeto == null) {
            texto = "Objeto Nulo";
        } else if (objeto instanceof PanacheEntity) {
            texto = "Classe: [" + objeto.getClass().getName();
            texto += "] id: [";
            texto += ((PanacheEntity) objeto).id;
            texto += "]";
        } else if (objeto instanceof String) {
            texto = (String) objeto;
        } else {
            texto = String.valueOf(objeto);
        }
        return texto;
    }


    public static String preencheEspacos(String texto, String preenchimento, Integer tamanho, Integer tipo) {
        StringBuilder sb = new StringBuilder();
        if (texto == null) {
            throw new IllegalArgumentException("Texto passado ao preencheEspacos esta nulo.");
        }
        switch (tipo) {
            case 1:
                if (tamanho > texto.length()) {
                    sb.append(repeatString(preenchimento, tamanho - texto.length()));
                    sb.append(texto);
                } else {
                    sb.append(texto);
                }
                break;
            case 2:
                sb.append(texto);
                sb.append(repeatString(preenchimento, tamanho));
                break;
            default:
                throw new IllegalArgumentException("Tipo não esperado: " + tipo);
        }
        sb.setLength(tamanho);
        return sb.toString();
    }

    public static String preencheEspacos(String texto, String preenchimento, Integer tamanho) {
        return preencheEspacos(texto, preenchimento, tamanho, 2);
    }

    public static String preencheEspacosFim(String texto, String preenchimento, Integer tamanho) {
        return preencheEspacos(texto, preenchimento, tamanho, 2);
    }

    public static String preencheEspacosInicio(String texto, String preenchimento, Integer tamanho) {
        return preencheEspacos(texto, preenchimento, tamanho, 1);
    }
}