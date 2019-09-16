package br.com.done.done.service;

import br.com.done.done.dto.PagamentoSearchDto;
import br.com.done.done.model.Pagamento;
import br.com.done.done.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.extensions.XSSFHeaderFooter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ExcelGeneratorService {

    @Autowired
    private PagamentoService pagamentoService;

    private XSSFCellStyle styleGroupOne;
    private XSSFCellStyle styleGroupTwo;
    private XSSFCellStyle styleGroupThree;
    private XSSFCellStyle styleGroupFour;
    private XSSFCellStyle styleHeader;
    private XSSFCellStyle styleHeaderBlack;
    private XSSFCellStyle styleDefault;
    private XSSFCellStyle styleDefaultOdd;
    private XSSFCellStyle styleAlinhado;

    public ByteArrayInputStream pagamentosToExcell(PagamentoSearchDto pagSearchDto) throws IOException {

        pagSearchDto.setPage(0);
        pagSearchDto.setSize(1001);
        Page<Pagamento> pagamentos = pagamentoService.findAll(pagSearchDto);
        if (pagamentos.getTotalElements() == 0 || pagamentos.getTotalElements() > 1000) {
            throw new IOException("");
        } else {
            boolean isNotLast = true;

            try (XSSFWorkbook workbook = new XSSFWorkbook();
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {

                XSSFSheet sheet = workbook.createSheet("Pagamentos");

                sheet.setMargin(XSSFSheet.RightMargin, 0.4);
                sheet.setMargin(XSSFSheet.LeftMargin, 0.4);
                sheet.setMargin(XSSFSheet.TopMargin, 0.4);
                sheet.setMargin(XSSFSheet.BottomMargin, 0.4);

                XSSFPrintSetup layout = sheet.getPrintSetup();
                layout.setLandscape(true);
                layout.setFitWidth((short) 1);
                layout.setFitHeight((short) 0);
                layout.setPaperSize(PrintSetup.A4_PAPERSIZE);
                layout.setFooterMargin(0.25);

                XSSFRow row;
                int rowAtual = 6;
                XSSFCell cell;
                int cellAtual;

                setStyle(workbook);

                // ordem de agrupamento
                String grupo1 = "Orgão";
                String grupo2 = "Credor";

                String valueGroupOne = "";
                String valueGroupTwo = "";

                Double totalG1 = 0.0;
                Double totalG2 = 0.0;
                Double totalGeral = 0.0;

                int rowValorOne = 0;
                int rowValorTwo = 0;

                int odd = 0;
                int count = 1;

                do {
                    List<Pagamento> pagamento = pagamentos.getContent();
                    for (int i = 0; i < pagamento.size(); i++) {

                        odd++;

                        boolean isCreateRow = false;

                        String chaveGrupo1 = pagamento.get(i).getOrgao().getCodigo()
                                + " - " + pagamento.get(i).getOrgao().getNome();

                        if (!valueGroupOne.equalsIgnoreCase(chaveGrupo1)) {

                            if (count > 1) {
                                cell = sheet.getRow(rowValorOne).createCell(6);
                                cell.setCellValue(totalG1);
                                cell.setCellStyle(styleGroupOne);

                                totalG1 = 0D;
                                sheet.createRow(rowAtual++);
                                sheet.createRow(rowAtual++);
                                isCreateRow = true;
                            }

                            valueGroupOne = chaveGrupo1;

                            rowValorOne = rowAtual;

                            row = sheet.createRow(rowAtual++);
                            cellAtual = 0;
                            cell = row.createCell(cellAtual++);
                            cell.setCellValue(grupo1);
                            cell.setCellStyle(styleGroupOne);

                            cell = row.createCell(cellAtual++);
                            cell.setCellValue(valueGroupOne);
                            cell.setCellStyle(styleGroupOne);
                            cell = row.createCell(cellAtual++);
                            cell.setCellStyle(styleGroupOne);
                            cell = row.createCell(cellAtual++);
                            cell.setCellStyle(styleGroupOne);
                            cell = row.createCell(cellAtual++);
                            cell.setCellStyle(styleGroupOne);
                            cell = row.createCell(cellAtual++);
                            cell.setCellStyle(styleGroupOne);

                            sheet.addMergedRegion(new CellRangeAddress(rowAtual - 1, rowAtual - 1, 1, 5));
                            sheet.createRow(rowAtual++);
                        }

                        StringBuilder chaveGrupo2 = new StringBuilder();
                        chaveGrupo2.append(chaveGrupo1);
                        String valueGroupTwoAux = pagamento.get(i).getCredor().getNome();
                        chaveGrupo2.append(valueGroupTwoAux);

                        if (!valueGroupTwo.equalsIgnoreCase(chaveGrupo2.toString())) {
                            odd = 1;
                            if (count > 1) {
                                cell = sheet.getRow(rowValorTwo).createCell(6);
                                cell.setCellValue(totalG2);
                                cell.setCellStyle(styleGroupTwo);

                                totalG2 = 0D;

                                if (!isCreateRow) {
                                    sheet.createRow(rowAtual++);
                                }
                            }
                            rowValorTwo = rowAtual;

                            valueGroupTwo = chaveGrupo2.toString();

                            row = sheet.createRow(rowAtual++);
                            cellAtual = 0;
                            cell = row.createCell(cellAtual++);
                            cell.setCellValue(grupo2);
                            cell.setCellStyle(styleGroupTwo);

                            cell = row.createCell(cellAtual++);
                            cell.setCellValue(valueGroupTwoAux + " (id: " + pagamento.get(i).getCredor().getId() + ")");
                            cell.setCellStyle(styleGroupTwo);
                            cell = row.createCell(cellAtual++);
                            cell.setCellStyle(styleGroupTwo);
                            cell = row.createCell(cellAtual++);
                            cell.setCellStyle(styleGroupTwo);
                            cell = row.createCell(cellAtual++);
                            cell.setCellStyle(styleGroupTwo);
                            cell = row.createCell(cellAtual++);
                            cell.setCellStyle(styleGroupTwo);

                            sheet.addMergedRegion(new CellRangeAddress(rowAtual - 1, rowAtual - 1, 1, 5));

                            row = sheet.createRow(rowAtual++);
                            cellAtual = 0;

                            cell = row.createCell(cellAtual++);
                            cell.setCellValue("Data");
                            cell.setCellStyle(styleHeaderBlack);

                            cell = row.createCell(cellAtual++);
                            cell.setCellValue("OB");
                            cell.setCellStyle(styleHeaderBlack);

                            cell = row.createCell(cellAtual++);
                            cell.setCellValue("NL");
                            cell.setCellStyle(styleHeaderBlack);

                            cell = row.createCell(cellAtual++);
                            cell.setCellValue("NE");
                            cell.setCellStyle(styleHeaderBlack);

                            cell = row.createCell(cellAtual++);
                            cell.setCellValue("Fonte");
                            cell.setCellStyle(styleHeaderBlack);

                            cell = row.createCell(cellAtual++);
                            cell.setCellValue("Classificação");
                            cell.setCellStyle(styleHeaderBlack);

                            cell = row.createCell(cellAtual++);
                            cell.setCellValue("Valor");
                            cell.setCellStyle(styleHeaderBlack);

                        }

                        row = sheet.createRow(rowAtual++);
                        cellAtual = 0;
                        cell = row.createCell(cellAtual++);
                        cell.setCellValue(pagamento.get(i).getData().toString());
                        if (odd % 2 == 0) {
                            cell.setCellStyle(styleDefaultOdd);
                        } else {
                            cell.setCellStyle(styleDefault);
                        }
                        cell = row.createCell(cellAtual++);
                        //nr_ob
                        if (pagamento.get(i).getNrOb() != null && pagamento.get(i).getNrOb().length() > 7) {
                            cell.setCellValue(pagamento.get(i).getNrOb().substring(6));
                        } else {
                            cell.setCellValue("-");
                        }

                        if (odd % 2 == 0) {
                            cell.setCellStyle(styleDefaultOdd);
                        } else {
                            cell.setCellStyle(styleDefault);
                        }

                        cell = row.createCell(cellAtual++);
                        if (pagamento.get(i).getNrNl() != null && pagamento.get(i).getNrNl().length() > 7) {
                            cell.setCellValue(pagamento.get(i).getNrNl().substring(6));
                        } else {
                            cell.setCellValue("-");
                        }
                        if (odd % 2 == 0) {
                            cell.setCellStyle(styleDefaultOdd);
                        } else {
                            cell.setCellStyle(styleDefault);
                        }
                        cell = row.createCell(cellAtual++);
                        if (pagamento.get(i).getNrNe() != null && pagamento.get(i).getNrNe().length() > 7) {
                            cell.setCellValue(pagamento.get(i).getNrNe().substring(6));
                        } else {
                            cell.setCellValue("-");
                        }
                        if (odd % 2 == 0) {
                            cell.setCellStyle(styleDefaultOdd);
                        } else {
                            cell.setCellStyle(styleDefault);
                        }
                        cell = row.createCell(cellAtual++);
                        cell.setCellValue(
                                pagamento.get(i).getFonte().getId() + "-"
                                        + pagamento.get(i).getFonte().getNome());
                        if (odd % 2 == 0) {
                            cell.setCellStyle(styleDefaultOdd);
                        } else {
                            cell.setCellStyle(styleDefault);
                        }
                        cell = row.createCell(cellAtual++);
                        cell.setCellValue(pagamento.get(i).getClassificacao().getNome());
                        if (odd % 2 == 0) {
                            cell.setCellStyle(styleDefaultOdd);
                        } else {
                            cell.setCellStyle(styleDefault);
                        }

                        cell = row.createCell(cellAtual++);
                        Double valor = Util.strToBigDecimal(pagamento.get(i).getValor().toString()).doubleValue();
                        cell.setCellValue(valor);
                        if (odd % 2 == 0) {
                            cell.setCellStyle(styleDefaultOdd);
                        } else {
                            cell.setCellStyle(styleDefault);
                        }

                        cell.getRow().setHeight((short) -1);

                        totalG1 += valor;
                        totalG2 += valor;
                        totalGeral += valor;

                        count++;
                    }

                    if (pagamentos.isLast()) {
                        isNotLast = false;

                        cell = sheet.getRow(rowValorOne).createCell(6);
                        cell.setCellValue(totalG1);
                        cell.setCellStyle(styleGroupOne);

                        cell = sheet.getRow(rowValorTwo).createCell(6);
                        cell.setCellValue(totalG2);
                        cell.setCellStyle(styleGroupTwo);

                        row = sheet.createRow(4);
                        cell = row.createCell(0);
                        cell.setCellValue("Total Geral");
                        cell.setCellStyle(styleDefaultOdd);

                        cell = row.createCell(1);
                        cell.setCellValue(totalGeral);
                        cell.setCellStyle(styleDefaultOdd);

                        cell = row.createCell(2);
                        cell.setCellStyle(styleDefaultOdd);
                        sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 4));

                        row = sheet.createRow(0);
                        cell = row.createCell(0);
                        cell.setCellValue("Relação de pagamentos");

                        createHead(pagSearchDto, sheet);
                    } else {
                        pagSearchDto.setPage(pagSearchDto.getPage() + 1);
                        pagamentos = pagamentoService.findAll(pagSearchDto);
                    }

                } while (isNotLast);

                sheet.setColumnWidth(0, 2500);
                sheet.setColumnWidth(1, 1700);
                sheet.setColumnWidth(2, 1700);
                sheet.setColumnWidth(3, 1700);
                sheet.setColumnWidth(4, 11900);
                sheet.setColumnWidth(5, 11900);
                sheet.setColumnWidth(6, 4000);

                //numero de pagina
                XSSFHeaderFooter header = (XSSFHeaderFooter) sheet.getFooter();
                header.setLeft(HSSFHeader.font("Calibri", "Normal")
                        + HSSFHeader.fontSize((short) 9)
                        + "Deputado Dermilson Chagas");
                header.setRight(HSSFHeader.font("Calibri", "Normal")
                        + HSSFHeader.fontSize((short) 9) + "Página "
                        + HeaderFooter.page() + " de " + HeaderFooter.numPages());

                workbook.write(out);
                return new ByteArrayInputStream(out.toByteArray());
            }
        }
    }

    private void createHead(PagamentoSearchDto pagSearchDto, XSSFSheet sheet) {

        StringBuilder vl = new StringBuilder("Valores ");
        String e = "";

        if (pagSearchDto.getValorInicial() != null && !pagSearchDto.getValorInicial().isEmpty()) {
            vl.append("acima de ");
            vl.append(pagSearchDto.getValorInicial());
            e = " e ";
        }
        if (pagSearchDto.getValorFinal() != null && !pagSearchDto.getValorFinal().isEmpty()) {
            vl.append(e);
            vl.append("abaixo de ");
            vl.append(pagSearchDto.getValorFinal());
        }
        int r = 3;
        int c = 4;

        if (vl.length() > 10) {
            XSSFRow row = sheet.createRow(r);
            XSSFCell cell = row.createCell(0);
            cell.setCellValue(vl.toString());
            cell.setCellStyle(styleDefaultOdd);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 0, c));
            r = 2;
        }

        StringBuilder pe = new StringBuilder("Período");
        if (pagSearchDto.getDataInicial() != null && pagSearchDto.getDataInicial().toString().length() > 0) {
            pe.append(" de ");
            pe.append(pagSearchDto.getDataInicial());

        }
        if (pagSearchDto.getDataFinal() != null && pagSearchDto.getDataFinal().toString().length() > 0) {
            pe.append(" até ");
            pe.append(pagSearchDto.getDataFinal());
        }
        if (pe.length() > 10) {
            XSSFRow row = sheet.createRow(r);
            XSSFCell cell = row.createCell(0);
            cell.setCellValue(pe.toString());
            cell.setCellStyle(styleDefaultOdd);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 0, c));
        }
    }

    private void setStyle(XSSFWorkbook wb) {
        setStyleGroupOne(wb);
        setStyleGroupTwo(wb);
        setStyleGroupThree(wb);
        setStyleGroupFour(wb);
        setStyleHeader(wb);
        setStyleHeaderBlack(wb);
        setStyleDefault(wb);
        setStyleDefaultOdd(wb);
        setStyleAlinhado(wb);
    }

    /**
     * @param wb
     */
    private void setStyleGroupOne(XSSFWorkbook wb) {
        styleGroupOne = wb.createCellStyle();
        styleGroupOne.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
        styleGroupOne.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleGroupOne.setDataFormat((short) 7);
    }

    /**
     * @param wb
     */
    private void setStyleGroupTwo(XSSFWorkbook wb) {
        styleGroupTwo = wb.createCellStyle();
        styleGroupTwo.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        styleGroupTwo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleGroupTwo.setDataFormat((short) 7);
    }

    /**
     * @param wb
     */
    private void setStyleGroupThree(XSSFWorkbook wb) {
        styleGroupThree = wb.createCellStyle();
        styleGroupThree.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        styleGroupThree.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleGroupThree.setDataFormat((short) 7);
    }

    /**
     * @param wb
     */
    private void setStyleGroupFour(XSSFWorkbook wb) {
        styleGroupFour = wb.createCellStyle();
        styleGroupFour.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        styleGroupFour.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleGroupFour.setDataFormat((short) 7);
    }

    /**
     * @param wb
     */
    private void setStyleHeader(XSSFWorkbook wb) {
        styleHeader = wb.createCellStyle();
        styleHeader.setBorderBottom(BorderStyle.THIN);
        styleHeader.setBorderTop(BorderStyle.THIN);
        styleHeader.setBorderLeft(BorderStyle.THIN);
        styleHeader.setBorderRight(BorderStyle.THIN);
        styleHeader.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    /**
     * @param wb
     */
    private void setStyleHeaderBlack(XSSFWorkbook wb) {
        styleHeaderBlack = wb.createCellStyle();
        styleHeaderBlack.setFillForegroundColor(IndexedColors.BLACK.getIndex());
        styleHeaderBlack.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = wb.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        styleHeaderBlack.setFont(font);
    }

    /**
     * @param wb
     */
    private void setStyleDefault(XSSFWorkbook wb) {
        styleDefault = wb.createCellStyle();
        styleDefault.setDataFormat((short) 7);
        styleDefault.setVerticalAlignment(VerticalAlignment.CENTER);
        styleDefault.setAlignment(HorizontalAlignment.JUSTIFY);
    }

    /**
     * @param wb
     */
    private void setStyleDefaultOdd(XSSFWorkbook wb) {
        styleDefaultOdd = wb.createCellStyle();
        styleDefaultOdd.setDataFormat((short) 7);
        styleDefaultOdd.setVerticalAlignment(VerticalAlignment.CENTER);
        styleDefaultOdd.setAlignment(HorizontalAlignment.JUSTIFY);
        styleDefaultOdd.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        styleDefaultOdd.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    /**
     * @param wb
     */
    private void setStyleAlinhado(XSSFWorkbook wb) {
        styleAlinhado = wb.createCellStyle();
        styleAlinhado.setVerticalAlignment(VerticalAlignment.TOP);
        styleAlinhado.setAlignment(HorizontalAlignment.JUSTIFY);
    }

}
